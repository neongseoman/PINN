package com.ssafy.be.common.component;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    ConcurrentHashMap<Integer, Team> teams;
    private int gameId;
    private int themeId;
    private String leaderId;
    private int roundCount;
    private int stage1Time;
    private int stage2Time;
    private LocalDateTime roomCreateTime;
    private LocalDateTime startedTime;
    private LocalDateTime finishedTime;
    private boolean hasPassword;


}
