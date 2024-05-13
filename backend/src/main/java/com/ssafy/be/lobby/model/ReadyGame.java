package com.ssafy.be.lobby.model;

import com.ssafy.be.common.component.GameStatus;
import com.ssafy.be.common.component.TeamComponent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReadyGame {
    private List<SearchTeam> teams;
    private int gameId;
    private String roomName;
    private int themeId;
    private int leaderId;
    private int roundCount;
//    private int currentRound;// 이거 추가하면 어때?
    private int stage1Time;
    private int stage2Time;
    private LocalDateTime roomCreateTime;
//    private LocalDateTime startedTime;
//    private LocalDateTime finishedTime;
    private boolean password;
    private GameStatus status;
    private final int TeamCount = 10;
}
