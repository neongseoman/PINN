package com.ssafy.be.common.component;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Team implements GameAccessAuth{
    HashMap<Long, TeamGamer> teamGamers;
    private int teamId;
    private int gameId;
    private int colorId;
    private int teamNumber;
    private boolean isReady;
    private LocalDateTime lastReadyTime;
    private int finalRank;
    private int finalScore;
}
