package com.ssafy.be.lobby.service;

import com.ssafy.be.common.Provider.ColorCode;
import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.TeamComponent;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.model.repository.GameRepository;
import com.ssafy.be.lobby.model.dto.CreateRoomDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class LobbyServiceImpl implements LobbyService {

    private final int teamCount = 10;

    @Autowired
    GameRepository gameRepository;

    @Override
    public GameComponent createRoom(CreateRoomDTO createRoomDTO) {
        String password = createRoomDTO.getPassword();
        int hasPassword = 0;
        if(password != null && !password.isEmpty()){
            hasPassword = 1;
        }

        log.info("password : " + password);
        log.info("hasPassword : " + (password != null && !password.isEmpty()));

        Game savedGame = gameRepository.save(Game.builder()
                        .themeId(createRoomDTO.getThemeId())
                        .stage1Time(createRoomDTO.getStage1Time())
                        .stage2Time(createRoomDTO.getStage2Time())
                        .roundCount(createRoomDTO.getRoundCount())
                        .leaderId(createRoomDTO.getLeader_id())
                        .roomName(createRoomDTO.getRoomName())
                        .hasPassword(hasPassword)
                .build());
//        System.out.println(savedGame.getGameId());
        // password 삽입
        GameComponent gameComponent = savedGame.toGameComponent();
        gameComponent.setPassword(createRoomDTO.getPassword());
        return gameComponent;
    }

    @Override
    public void createTeams(GameComponent savedGame) {
        // TeamComponent 객체 생성 및 연결
        savedGame.setTeams(new ConcurrentHashMap<>());
        ConcurrentHashMap<Integer, TeamComponent> teams = savedGame.getTeams();
        // teamCount 만큼 팀 생성 및 색상, 번호 부여
        ColorCode[] colorcodes = ColorCode.values();
        for(int i=0; i<teamCount; i++){
            TeamComponent teamComponent = TeamComponent.builder()
                    .gameId(savedGame.getGameId())
                    .colorCode(colorcodes[i].getColorCode())
                    .teamId(colorcodes[i].getTeamNumber())
                    .teamNumber(colorcodes[i].getTeamNumber())
                    .isReady(false)
                    .build();
            log.info(teamComponent);

            teams.put(colorcodes[i].getTeamNumber(), teamComponent);
        }
    }

}
