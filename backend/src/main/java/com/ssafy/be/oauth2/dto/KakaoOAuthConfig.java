package com.ssafy.be.oauth2.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.client.kakao")
public record KakaoOAuthConfig(
        String client_id,
        String redirect_uri,
        String client_secret
) {}
