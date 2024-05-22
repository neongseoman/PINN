package com.ssafy.be.game.model.dto;

import com.ssafy.be.common.model.dto.SocketDTO;
import lombok.*;

@ToString
@Getter
@Setter
public class GameStartRequestDTO extends SocketDTO {

    private int gameId;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    private int scorePageTime;

    public GameStartRequestDTO(String senderNickname, int senderGameId, int senderTeamId, int gameId, int roundCount, int stage1Time, int stage2Time, int scorePageTime) {
        super(senderNickname, senderGameId, senderTeamId);
        setGameId(gameId);
        setRoundCount(roundCount);
        setStage1Time(stage1Time);
        setStage2Time(stage2Time);
        setScorePageTime(scorePageTime);
    }
}