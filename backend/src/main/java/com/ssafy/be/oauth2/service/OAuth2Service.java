package com.ssafy.be.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    @Value("${spring.security.oauth2.client.registration.kakao.client_id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect_uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client_secret}")
    private String clientSecret;

    private static WebClient webClient = WebClient.builder().build();
    private static final String kakaoTokenURL = "https://kauth.kakao.com/oauth/token";


    public String getAccessToken(String authCode) {
        MultiValueMap<String, String> params = queryParam(authCode);
        webClient.post();


    }

    private MultiValueMap<String,String> queryParam(String authCode){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", authCode);
        params.add("client_secret",clientSecret);

        return params;
    }
}
