package com.ssafy.be.common.component;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ToString
@Getter
@Setter  // TODO : 유효성검사
@Builder
public class GameComponent {
    ConcurrentHashMap<Integer, TeamComponent> teams;
    private int gameId;
    private String roomName;
    private int themeId;
    private int leaderId;
    private int roundCount;
    private int currentRound;// 이거 추가하면 어때?
    private int stage1Time;
    private int stage2Time;
    private LocalDateTime roomCreateTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private String password;
    private GameStatus status;
    private final int TeamCount = 10;

    private List<QuestionComponent> questions; // 이 게임에서 출제될 문제 목록
    private List<List<TeamRoundComponent>> roundResults; // 라운드별 결과 리스트
}
