package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.model.vo.*;

public interface GameService {
    /*
    for Game Scheduler
     */
    GameStartVO startGame(int gamerId, GameStartRequestDTO gameStartRequestDTO) throws BaseException;

    GameInitVO initGame(int gamerId, GameStartRequestDTO gameInitRequestDTO) throws BaseException;

    RoundFinishVO finishRound(RoundFinishRequestDTO roundFinishRequestDTO) throws BaseException;

    GameFinishVO finishGame(SocketDTO gameFinishRequestDTO) throws BaseException; // not implemented

    /*
    for Socket
     */
    PinMoveVO movePin(int gamerId, PinMoveRequestDTO pinMoveRequestDTO) throws BaseException;

    PinGuessVO guessPin(int gamerId, PinGuessRequestDTO pinGuessRequestDTO) throws BaseException;

    /*
    for REST API
     */
    GameInitVO getGameInfo(int gamerId, int gameId) throws BaseException;

    RoundInitVO findStage1Info(int gamerId, RoundInitRequestDTO roundInitRequestDTO) throws BaseException;

    Stage2InitVO findStage2Info(int gamerId, Stage2InitRequestDTO stage2InitRequestDTO) throws BaseException;

    RoundResultVO getRoundResult(int gamerId, RoundResultRequestDTO roundFinishRequestDTO) throws BaseException;

    GameResultVO getGameResult(int gamerId, GameResultRequestDTO gameResultRequestDTO) throws BaseException; // not implemented


}
