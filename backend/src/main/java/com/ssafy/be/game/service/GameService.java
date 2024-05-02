package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.game.model.dto.*;

public interface GameService {
    GameInitResponseDTO findGameInfo(GameInitRequestDTO gameInitRequestDTO) throws BaseException;

    RoundInitResponseDTO findStage1Info(RoundInitRequestDTO roundInitRequestDTO) throws BaseException;

    Stage2InitResponseDTO findStage2Info(Stage2InitRequestDTO stage2InitRequestDTO) throws BaseException;
}
