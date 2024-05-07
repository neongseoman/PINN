package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.model.vo.*;

public interface GameService {

    GameStartVO startGame(int gamerId, GameStartRequestDTO gameStartRequestDTO) throws BaseException;

    GameInitVO initGame(int gamerId, GameInitRequestDTO gameInitRequestDTO) throws BaseException;

    RoundInitVO findStage1Info(int gamerId, RoundInitRequestDTO roundInitRequestDTO) throws BaseException;

    Stage2InitVO findStage2Info(int gamerId, Stage2InitRequestDTO stage2InitRequestDTO) throws BaseException;

    PinMoveVO movePin(int gamerId, PinMoveRequestDTO pinMoveRequestDTO) throws BaseException;

    PinGuessVO guessPin(int gamerId, PinGuessRequestDTO pinGuessRequestDTO) throws BaseException;
}
