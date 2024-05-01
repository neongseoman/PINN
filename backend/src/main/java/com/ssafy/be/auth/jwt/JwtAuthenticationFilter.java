package com.ssafy.be.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("enter jwt filter");
        Cookie[] cookies =request.getCookies();
        if(cookies == null) {
            throw new ServletException("No cookies found");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access_token")) {
                String token = cookie.getValue();
                UsernamePasswordAuthenticationToken authenticationToken = jwtProvider.getAuthentication(token);
                if (authenticationToken != null) {
                    log.info("pass authentication");
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    request.setAttribute("gamerId",authenticationToken.getPrincipal());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
