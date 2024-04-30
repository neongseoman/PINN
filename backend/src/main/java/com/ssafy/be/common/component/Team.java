package com.ssafy.be.common.component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class Team {
    ConcurrentHashMap<Long, TeamGamer> teamGamers;
    private int teamId;
    private int gameId;
    private int colorId;
    private int teamNumber;
    private boolean isReady;
    private LocalDateTime lastReadyTime;
    private int finalRank;
    private int finalScore;
}
