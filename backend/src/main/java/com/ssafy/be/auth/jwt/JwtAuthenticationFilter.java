package com.ssafy.be.auth.jwt;

import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.gamer.repository.GamerLoginRedisRepository;
import com.ssafy.be.gamer.repository.GamerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private GamerLoginRedisRepository gamerLoginRedisRepository;
    private GamerRepository gamerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWT Authentication Filter");
        String token = jwtProvider.resolveToken(request);
        if (token != null && token.startsWith("Bearer ")) { // Auth 토큰이 있으면 검증한다.
            token = token.split(" ")[1];
            UsernamePasswordAuthenticationToken authenticationToken = jwtProvider.getAuthentication(token);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            GamerPrincipalVO gamerPrincipalVO =(GamerPrincipalVO)authenticationToken.getPrincipal();
            request.setAttribute("gamerPrincipal", gamerPrincipalVO);
        }
//        log.info("pass");
        filterChain.doFilter(request, response);
    }
}
