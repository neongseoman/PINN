package com.ssafy.be.lobby.service;

import static com.ssafy.be.common.response.BaseResponseStatus.CREATE_GAME_ERROR;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.model.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LobbyServiceImpl implements LobbyService {

    @Autowired
    GameRepository gameRepository;

    @Override
    public void createRoom(GameComponent game) {
        Integer game_id = gameRepository.save(Game.builder()
                        .themeId(game.getThemeId())
                        .hasPassword(game.isHasPassword())
                        .stage1Time(game.getStage1Time())
                        .stage2Time(game.getStage2Time())
                        .roundCount(game.getRoundCount())
                        .leaderId(game.getLeaderId())
                        .roomName(game.getRoomName())
                .build()).getGameId();
        if (game_id == null){
            throw new BaseException(CREATE_GAME_ERROR);
        }
    }
}
