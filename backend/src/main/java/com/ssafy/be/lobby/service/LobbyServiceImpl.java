package com.ssafy.be.lobby.service;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.model.repository.GameRepository;
import com.ssafy.be.lobby.model.dto.CreateRoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LobbyServiceImpl implements LobbyService {

    @Autowired
    GameRepository gameRepository;

    @Override
    public GameComponent createRoom(CreateRoomDTO createRoomDTO) {
        boolean hasPassword = !createRoomDTO.getPassword().isEmpty();
        System.out.println(createRoomDTO);
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
        return savedGame.toGameComponent();
    }
}
