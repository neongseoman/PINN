package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.model.repository.GameRepository;
import com.ssafy.be.game.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Autowired
    private GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /////

    @Override
    public GameInitResponseDTO findGameInfo(GameInitRequestDTO gameInitRequestDTO) throws BaseException {
        // TODO 게임 시작 요청 시 테마에 해당하는 문제 랜덤 배정 + GameAndQuestion table에 insert
        int gameId = gameInitRequestDTO.getGameId();

        Game existGame = gameRepository.findById(gameId).orElse(null);

        if (existGame == null) {
            throw new BaseException(null);
        }

        try {
            // game의 정보 중 필요한 것들을 gIRD에 담아서 return
            GameInitResponseDTO gameInitResponseDTO = new GameInitResponseDTO();
            gameInitResponseDTO.setGameId(existGame.getGameId());
            gameInitResponseDTO.setRoomName(existGame.getRoomName());
            gameInitResponseDTO.setLeaderId(existGame.getLeaderId());
            gameInitResponseDTO.setRoundCount(existGame.getRoundCount());
            gameInitResponseDTO.setThemeId(existGame.getThemeId());
            gameInitResponseDTO.setStage1Time(existGame.getStage1Time());
            gameInitResponseDTO.setStage2Time(existGame.getStage2Time());
            gameInitResponseDTO.setStartedTime(existGame.getStartedTime());

            return gameInitResponseDTO;
        } catch (Exception e) {
            throw new BaseException(null);
        }
    }

    @Override
    public RoundInitResponseDTO findStage1Info(RoundInitRequestDTO roundInitRequestDTO) throws BaseException {

        // TODO: 해당 라운드의 문제 + 스테이지1에 해당하는 힌트 return

        Game existGame = gameRepository.findById(roundInitRequestDTO.getGameId()).orElse(null);

        if (existGame == null) {
            throw new BaseException(null);
        }

        return null;
    }

    @Override
    public Stage2InitResponseDTO findStage2Info(Stage2InitRequestDTO stage2InitRequestDTO) throws BaseException {
        //
        Game existGame = gameRepository.findById(stage2InitRequestDTO.getGameId()).orElse(null);

        if (existGame == null) {
            throw new BaseException(null);
        }

        try {
            Stage2InitResponseDTO stage2InitResponseDTO = new Stage2InitResponseDTO();
            // 힌트2

            return stage2InitResponseDTO;
        } catch (Exception e) {
            throw new BaseException(null);
        }
    }


}
