package com.ssafy.be.game.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoundRequestDTO /*extends SocketDTO*/ {

    private int gameId;
    private int round;

//    public RoundRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int gameId, int round) {
//        super(senderNickname, senderGameId, senderTeamId);
//        this.gameId = gameId;
//        this.round = round;
//    }
}