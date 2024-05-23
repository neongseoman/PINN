package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.game.model.dto.entitys.GameQuestionDTO;

import java.util.List;

public interface GameQuestionService {

    int createGameQuestion(GameQuestionDTO gameQuestionDTO) throws BaseException;

    int updateGameQuestion(GameQuestionDTO gameQuestionDTO) throws BaseException;

    int deleteGameQuestion(int gameQuestionId) throws BaseException;

    GameQuestionDTO getGameQuestion(int gameQuestionId) throws BaseException;

    List<GameQuestionDTO> getAllGameQuestionByGameId(int gameId) throws BaseException;

    List<GameQuestionDTO> getAllGameQuesitonByQuestionId(int questionId) throws BaseException;
}
