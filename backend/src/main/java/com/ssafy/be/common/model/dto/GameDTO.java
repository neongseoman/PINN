package com.ssafy.be.common.model.dto;

import com.ssafy.be.common.model.domain.Game;
import lombok.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameDTO {
    private int gameId;
    private String roomName;
    private int themeId;
    private int leaderId;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    private LocalDateTime roomCreateTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private int hasPassword;

    @Builder
    public GameDTO(Game game) {
        this.gameId = game.getGameId();
        this.roomName = game.getRoomName();
        this.themeId = game.getThemeId();
        this.leaderId = game.getLeaderId();
        this.roundCount = game.getRoundCount();
        this.stage1Time = game.getStage1Time();
        this.stage2Time = game.getStage2Time();
        this.roomCreateTime = game.getRoomCreateTime();
        this.startedTime = game.getStartedTime();
        this.finishedTime = game.getFinishedTime();
        this.hasPassword = game.getHasPassword();
    }

    public Game toEntity() {
        return Game.builder()
                .gameId(this.gameId)
                .roomName(this.roomName)
                .themeId(this.themeId)
                .leaderId(this.leaderId)
                .roundCount(this.roundCount)
                .stage1Time(this.stage1Time)
                .stage2Time(this.stage2Time)
                .roomCreateTime(this.roomCreateTime)
                .startedTime(this.startedTime)
                .finishedTime(this.finishedTime)
                .hasPassword(this.hasPassword)
                .build();
    }
}
