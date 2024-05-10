package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.*;

@Getter
@Setter
@ToString
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoundInitRequestDTO /*extends SocketDTO*/ {

    private int gameId;
    private int round;

//    public RoundInitRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int gameId, int round) {
//        super(senderNickname, senderGameId, senderTeamId);
//        this.gameId = gameId;
//        this.round = round;
//    }
}