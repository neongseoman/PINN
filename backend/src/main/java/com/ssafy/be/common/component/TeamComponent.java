package com.ssafy.be.common.component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class TeamComponent {
    ConcurrentHashMap<Long, TeamGamerComponent> teamGamers;
    private int teamId;
    private int gameId;
    private String colorCode;  // ex) 'rgba(251, 52, 159, 1)'
    private int teamNumber;
    private boolean isReady;
    private LocalDateTime lastReadyTime;
    private int finalRank;
    private int finalScore;
}
