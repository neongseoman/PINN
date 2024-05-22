package com.ssafy.be.game.model.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class GameInitVO {
    private int gameId;
    private String roomName;
    private int themeId;
    private int leaderId;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    private LocalDateTime startedTime;
}