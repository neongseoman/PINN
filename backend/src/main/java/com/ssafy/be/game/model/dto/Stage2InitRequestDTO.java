package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.*;

@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Stage2InitRequestDTO extends SocketDTO {
    private int gameId;
    private int round;

    public Stage2InitRequestDTO(String senderNickname, int senderGameId, int senderTeamId) {
        super(senderNickname, senderGameId, senderTeamId);
    }
}