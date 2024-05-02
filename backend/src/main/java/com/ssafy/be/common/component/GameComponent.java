package com.ssafy.be.common.component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ToString
@Getter
@Setter  // TODO : 유효성검사
public class GameComponent {
    ConcurrentHashMap<Integer, TeamComponent> teams;
    private int gameId;
    private String roomName;
    private int themeId;
    private String leaderId;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    private LocalDateTime roomCreateTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private boolean hasPassword;
    private String password;

    private List<QuestionComponent> questions; // 이 게임에서 출제될 문제 목록

}