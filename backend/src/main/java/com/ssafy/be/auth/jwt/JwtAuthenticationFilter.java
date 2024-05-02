package com.ssafy.be.auth.jwt;

import com.ssafy.be.auth.model.JwtPayload;
import com.ssafy.be.gamer.model.GamerDTO;
import com.ssafy.be.gamer.model.LoginTokenDTO;
import com.ssafy.be.gamer.repository.GamerLoginRedisRepository;
import com.ssafy.be.gamer.repository.GamerRepository;
import jakarta.persistence.Id;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private GamerLoginRedisRepository gamerLoginRedisRepository;
    @Autowired
    private GamerRepository gamerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("enter jwt filter");
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        Cookie[] cookies = request.getCookies();
        if (token != null && token.startsWith("Bearer ")) { // Auth 토큰이 있으면 검증한다.
            UsernamePasswordAuthenticationToken authenticationToken = jwtProvider.getAuthentication(token);
            if (authenticationToken != null) {
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                request.setAttribute("gamerId", authenticationToken.getPrincipal());
                doFilter(request, response, filterChain); // 정당한 유저야
            }

        } else { // 토큰이 없으면 refresh_token을 찾는다.
//            log.info("No Auth Token");
//            if (cookies != null) {
//                for (Cookie cookie : cookies) {
//                    if (cookie.getName().equals("refresh_token")) { //refresh가 있으면 access만들고 돌려보내.
//                        String refreshToken = cookie.getValue();
//                        Optional<LoginTokenDTO> tokenDTO = gamerLoginRedisRepository.findById(refreshToken);
//                        if (tokenDTO.isPresent()) {
//                            int gamerId = tokenDTO.get().getGamerId();
//                            GamerDTO gamerDTO = gamerRepository.findById(gamerId).orElseThrow(IllegalAccessError::new);
//                            String accessToken = jwtProvider.generateAccessToken(gamerDTO)[0];
//                            response.setHeader("access-token", accessToken);
//                        } else {
//                            response.sendRedirect("www.pinn.kr");
//                        }
//                        filterChain.doFilter(request, response);
//
//                    }
//                throw new ServletException("No cookies found"); // 로그인 시켜
//                }
//            }
        }

        filterChain.doFilter(request, response);
    }
}
