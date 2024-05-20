package com.ssafy.be.oauth2.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.gamer.model.GamerDTO;
import lombok.RequiredArgsConstructor;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record KakaoTokenDTO(
        String accessToken,
        String tokenType,
        String refreshToken,
        String expiresInt,
        String scope,
        String refreshTokenExpriesIn
) {

    public GamerDTO toGamer(){
        return new GamerDTO();
    }

}
