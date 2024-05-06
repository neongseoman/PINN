package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameInitRequestDTO extends SocketDTO {
    private int gameId;

    public GameInitRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int gameId) {
        super(senderNickname, senderGameId, senderTeamId);
        this.gameId = gameId;
    }
}