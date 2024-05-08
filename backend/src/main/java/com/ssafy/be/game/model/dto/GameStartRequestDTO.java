package com.ssafy.be.game.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.be.common.model.dto.SocketDTO;
import jakarta.persistence.Access;
import lombok.*;

@ToString
@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameStartRequestDTO extends SocketDTO {

    private int gameId;
    private int roundCount;
    private int stage1Time; // 일단 30으로 받자
    private int stage2Time; // 일단 30으로 받자.
    private int scorePageTime; //일단 15초로 보내줘

//    public GameStartRequestDTO() {}

//    public GameStartRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int gameId) {
//        super(senderNickname, senderGameId, senderTeamId);
//        setGameId(gameId);
//    }

    public GameStartRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int gameId, int roundCount, int stage1Time, int stage2Time, int scorePageTime) {
        super(senderNickname, senderGameId, senderTeamId);
        setGameId(gameId);
        setRoundCount(roundCount);
        setStage1Time(stage1Time);
        setStage2Time(stage2Time);
        setScorePageTime(scorePageTime);
    }
}