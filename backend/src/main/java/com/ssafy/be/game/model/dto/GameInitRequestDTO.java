package com.ssafy.be.game.model.dto;

import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class GameInitRequestDTO extends SocketDTO {
    private int gameId;

    public GameInitRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int gameId) {
        super(senderNickname, senderGameId, senderTeamId);
        this.gameId = gameId;
    }
}