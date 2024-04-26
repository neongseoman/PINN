package com.ssafy.be.auth.model;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Builder
@Log4j2
public record OAuth2UserInfo(
    String profile_nickname,
    String email
) {
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
//        return switch (registrationId){
//            case "kakao" -> ofKakao(attributes);
//            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
//
//        }
        return ofKakao(attributes);
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            log.info(entry.getKey() + ": " + entry.getValue());
        }
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        return OAuth2UserInfo.builder()
                .profile_nickname((String) account.get("profile_nickname"))
                .build();
    }
}

