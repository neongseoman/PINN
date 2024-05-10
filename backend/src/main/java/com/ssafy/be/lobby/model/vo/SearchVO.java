package com.ssafy.be.lobby.model.vo;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.TeamComponent;
import com.ssafy.be.common.component.TeamGamerComponent;
import com.ssafy.be.lobby.model.ReadyGame;
import com.ssafy.be.lobby.model.SearchTeam;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchVO {
    private ReadyGame readyGame;
    private int countPerson;

    public SearchVO(GameComponent gameComponent) {
        readyGame = ReadyGame.builder()
                .teams(new ArrayList<>())
                .gameId(gameComponent.getGameId())
                .roomName(gameComponent.getRoomName())
                .themeId(gameComponent.getThemeId())
                .leaderId(gameComponent.getLeaderId())
                .roundCount(gameComponent.getRoundCount())
                .stage1Time(gameComponent.getStage1Time())
                .stage2Time(gameComponent.getStage2Time())
                .roomCreateTime(gameComponent.getRoomCreateTime())
                .status(gameComponent.getStatus())
                .build();
        setTeams(gameComponent);
    }

    private void setTeams(GameComponent gameComponent) {
        for (TeamComponent teamComponent: gameComponent.getTeams().values()){
            SearchTeam searchTeam = SearchTeam.builder()
                    .teamGamers(new ArrayList<>())
                    .teamId(teamComponent.getTeamId())
                    .gameId(teamComponent.getGameId())
                    .colorCode(teamComponent.getColorCode())
                    .teamNumber(teamComponent.getTeamNumber())
                    .isReady(teamComponent.isReady())
                    .lastReadyTime(teamComponent.getLastReadyTime())
                    .build();

            ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = teamComponent.getTeamGamers();
            if (teamGamers != null){
                searchTeam.getTeamGamers().addAll(teamComponent.getTeamGamers().values());
            }

            this.readyGame.getTeams().add(searchTeam);
        }
    }
}
