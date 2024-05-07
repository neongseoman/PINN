package com.ssafy.be.common.component;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


@Builder
@ToString
@Getter
@Setter
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

    private ConcurrentHashMap<Integer, TeamRoundComponent> teamRounds; // 라운드 번호로 접근 (1,2,3 ...)
}
