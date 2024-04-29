package com.ssafy.be.oauth2.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "oauth2.client.kakao")
public record KakaoOAuthConfig(
        String client_id,
        String redirect_uri,
        String client_secret
) {}
