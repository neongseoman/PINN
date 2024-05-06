package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameStartRequestDTO extends SocketDTO {

    private int gameId;

    public GameStartRequestDTO(String senderNickname, int senderGameId, int senderTeamId,int gameId) {
        super(senderNickname, senderGameId, senderTeamId);
        setGameId(gameId);
    }
}