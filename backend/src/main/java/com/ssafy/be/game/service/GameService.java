package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.game.model.dto.*;

public interface GameService {

    GameStartResponseDTO startGame(int gamerId, GameStartRequestDTO gameStartRequestDTO) throws BaseException;

    GameInitResponseDTO initGame(int gamerId, GameInitRequestDTO gameInitRequestDTO) throws BaseException;

    RoundInitResponseDTO findStage1Info(int gamerId, RoundInitRequestDTO roundInitRequestDTO) throws BaseException;

    Stage2InitResponseDTO findStage2Info(int gamerId, Stage2InitRequestDTO stage2InitRequestDTO) throws BaseException;

    PinMoveResponseDTO movePin(int gamerId, PinMoveRequestDTO pinMoveRequestDTO) throws BaseException;

    PinGuessResponseDTO guessPin(int gamerId, PinGuessRequestDTO pinGuessRequestDTO) throws BaseException;
}
