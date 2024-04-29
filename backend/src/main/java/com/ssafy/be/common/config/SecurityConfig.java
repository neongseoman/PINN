package com.ssafy.be.common.config;

import com.ssafy.be.auth.service.OAuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {
    //jwt 처리 
    //oauth 처리
    private final OAuthServiceImpl oAuthServiceImpl;
    private static final String[] AUTH_BLACKLIST = {
            "/api/v1/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // request 인증, 인가 설정 url을 분석해서 인증 인가가 필요한 url의 인증을 요청한다.
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                AUTH_BLACKLIST
                        ).authenticated().anyRequest().permitAll());
//                //팀장 인가를 여기서하는게 나을 것도 같긴한데.........
//                .addFilterBefore(token)
//                .exceptionHandling((exception) ->
//                        exception
//                                .authenticationEntryPoint(new Authen))

        return http.build();
    }

//    CorsConfigurationSource corsConfigurationSource() {
//        return request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedHeaders(Collections.singletonList("*"));
//            config.setAllowedMethods(Collections.singletonList("*"));
//            config.setAllowedOriginPatterns(Arrays.asList("https://www.pinn.kr", "http://localhost:3000"));
//            config.setAllowCredentials(true);
//            config.addExposedHeader("accessToken");
//            config.addExposedHeader("refreshToken");
//            return config;
//        };
//    }

}
