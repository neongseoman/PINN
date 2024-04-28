package com.ssafy.be.oauth2.service;

import com.ssafy.be.oauth2.config.KakaoOAuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final KakaoOAuthConfig kakaoOAuthConfig;
    private static final String kakaoTokenURL = "https://kauth.kakao.com/oauth/token";
    WebClient.Builder tokenApi = WebClient.builder()
            .baseUrl(kakaoTokenURL)
            .defaultHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8");

    public String getAccessToken(String authCode) {
        MultiValueMap<String, String> params = queryParam(authCode);
        Map<String, Object> response = tokenApi.build()
                .post()
                .bodyValue(params)
                .retrieve()
                .bodyToMono(Map.class).block();

        for (Map.Entry<String, Object> stringObjectEntry : response.entrySet()) {
            log.info(stringObjectEntry.getKey()+":"+stringObjectEntry.getValue());
        }

        return "accessToken";
    }

    private MultiValueMap<String,String> queryParam(String authCode){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthConfig.client_id());
        params.add("redirect_uri", kakaoOAuthConfig.redirect_uri());
        params.add("code", authCode);
        params.add("client_secret",kakaoOAuthConfig.client_secret());

        return params;
    }
}
