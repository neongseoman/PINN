package com.ssafy.be.oauth2.dto;


public record KakaoAccessTokenDTO(
        String accessToken,
        String tokenType,
        String refreshToken,
        String expiresInt,
        String scope,
        String refreshTokenExpriesIn
) {
}
