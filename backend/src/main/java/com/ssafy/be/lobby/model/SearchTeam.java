package com.ssafy.be.lobby.model;

import com.ssafy.be.common.component.TeamGamerComponent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchTeam {
    List<TeamGamerComponent> teamGamers;
    private int teamId;
    private int gameId;
    private String colorCode;  // ex) 'rgba(251, 52, 159, 1)'
    private int teamNumber;
    private boolean isReady;
    private LocalDateTime lastReadyTime;
}
