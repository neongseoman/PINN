package com.ssafy.be.auth.handler;

import com.ssafy.be.auth.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private static final String URI = "/lobby";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String[] tokens = tokenProvider.generateAccessToken(authentication);
        String accessToken = tokens[0];
        String refreshToken = tokens[1];

        String redirectUrl = UriComponentsBuilder.fromHttpUrl(URI)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
