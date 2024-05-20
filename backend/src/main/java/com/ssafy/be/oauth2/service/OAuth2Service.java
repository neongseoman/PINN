package com.ssafy.be.oauth2.service;

import com.ssafy.be.common.Provider.NickNameProvider;
import com.ssafy.be.gamer.model.GamerDTO;
import com.ssafy.be.gamer.repository.GamerRepository;
import com.ssafy.be.oauth2.dto.KakaoOAuthConfig;
import com.ssafy.be.oauth2.dto.KakaoUserDTO;
import com.ssafy.be.oauth2.dto.OAuthType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final GamerRepository gamerRepository;
    private final NickNameProvider nickNameProvider;
    private final KakaoOAuthConfig kakaoOAuthConfig;
    private static final String kakaoTokenURL = "https://kauth.kakao.com/oauth/token";
    private static final String kakaoUserURL = "https://kapi.kakao.com/v2/user/me";
    WebClient.Builder tokenApi = WebClient.builder()
            .baseUrl(kakaoTokenURL)
            .defaultHeader("Content-type","application/x-www-form-urlencoded;charset=utf-8");

    WebClient.Builder userApi = WebClient.builder()
            .baseUrl(kakaoUserURL);

    public String getAccessTokenFromlocal(String authCode) {
        MultiValueMap<String, String> params = queryParam(authCode,kakaoOAuthConfig.local_redirect_uri());
        Map<String,Object> response = tokenApi.build()
                .post()
                .bodyValue(params)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String acessToken = response.get("access_token").toString();
        return acessToken;
    }
    public String getAccessTokenFromTest(String authCode, String redirectUri) {
        MultiValueMap<String, String> params = queryParam(authCode,redirectUri);
        Map<String,Object> response = tokenApi.build()
                .post()
                .bodyValue(params)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String acessToken = response.get("access_token").toString();
        return acessToken;
    }
    public String getAccessTokenFromServer(String authCode) {
        MultiValueMap<String, String> params = queryParam(authCode,kakaoOAuthConfig.server_redirect_uri());
        Map<String,Object> response = tokenApi.build()
                .post()
                .bodyValue(params)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String acessToken = response.get("access_token").toString();
        return acessToken;
    }

    public GamerDTO getUserInfo(String accessToken) {
        Map response = userApi.build()
                .post()
                .header("Content-type","application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> kakao_account = (Map<String, Object>) response.get("kakao_account");  // 카카오로 받은 데이터에서 계정 정보가 담긴 kakao_account 값을 꺼낸다.
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");   // 마찬가지로 profile(nickname, image_url.. 등) 정보가 담긴 값을 꺼낸다.

         KakaoUserDTO userDTO = new KakaoUserDTO(
                (Long) response.get("id"),
                (String) profile.get("nickname"));
        return getOrSave(userDTO);
    }


    private MultiValueMap<String,String> queryParam(String authCode,String uri){
//        log.info("re uri" + kakaoOAuthConfig.localRedirectUri());
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthConfig.client_id());
        params.add("redirectUri", uri);
        params.add("code", authCode);
        params.add("client_secret",kakaoOAuthConfig.client_secret());

        return params;
    }

    private GamerDTO getOrSave(KakaoUserDTO userDTO){
        GamerDTO gamer = gamerRepository.findByProviderId(userDTO.id()).orElse(null);

        if (gamer == null) {
            String nickname = nickNameProvider.randomNickName();
            gamer = new GamerDTO(
                    OAuthType.KAKAO,
                    userDTO.id(),
                    userDTO.nickname(),
                    nickname,
                    false
            );

            gamerRepository.save(gamer);
        }
        return gamer;
    }
}
