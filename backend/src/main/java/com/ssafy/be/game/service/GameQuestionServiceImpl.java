package com.ssafy.be.game.service;

import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.game.model.domain.GameQuestion;
import com.ssafy.be.game.model.dto.entity.GameQuestionDTO;
import com.ssafy.be.game.model.repository.GameQuestionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class GameQuestionServiceImpl implements GameQuestionService {

    private final GameQuestionRepository gameQuestionRepository;

    @Autowired
    private GameQuestionServiceImpl(GameQuestionRepository gameQuestionRepository) {
        this.gameQuestionRepository = gameQuestionRepository;
    }

    //////////

    @Override
    public int createGameQuestion(GameQuestionDTO gameQuestionDTO) throws BaseException {
        try {
            GameQuestion gameQuestion = gameQuestionDTO.toEntity();
            return gameQuestionRepository.save(gameQuestion).getGameQuestionId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public int updateGameQuestion(GameQuestionDTO gameQuestionDTO) throws BaseException {
        try {
            GameQuestion existGameQuestion = gameQuestionRepository.findById(gameQuestionDTO.getGameQuestionId()).orElse(null);
            if (existGameQuestion == null) throw new BaseException(BaseResponseStatus.OOPS);

            existGameQuestion.setGameId(gameQuestionDTO.getGameId());
            existGameQuestion.setQuestionId(gameQuestionDTO.getQuestionId());
            existGameQuestion.setRoundNumber(gameQuestionDTO.getRoundNumber());

            return gameQuestionRepository.save(existGameQuestion).getGameQuestionId();
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public int deleteGameQuestion(int gameQuestionId) throws BaseException {
        try {
            GameQuestion existGameQuestion = gameQuestionRepository.findById(gameQuestionId).orElse(null);
            if (existGameQuestion == null) throw new BaseException(BaseResponseStatus.OOPS);

            gameQuestionRepository.deleteById(gameQuestionId);
            return gameQuestionId;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public GameQuestionDTO getGameQuestion(int gameQuestionId) throws BaseException {
        try {
            GameQuestion existGameQuestion = gameQuestionRepository.findById(gameQuestionId).orElse(null);
            if (existGameQuestion == null) {
                throw new BaseException(BaseResponseStatus.OOPS);
            } else {
                return new GameQuestionDTO(existGameQuestion);
            }
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<GameQuestionDTO> getAllGameQuestionByGameId(int gameId) throws BaseException {
        try {
            List<GameQuestion> gameQuestionList = gameQuestionRepository.findAllByGameId(gameId);
            if (gameQuestionList == null) throw new BaseException(BaseResponseStatus.OOPS);

            List<GameQuestionDTO> gameQuestionDTOList = new ArrayList<>();
            for (GameQuestion gameQuestion : gameQuestionList) {
                gameQuestionDTOList.add(new GameQuestionDTO(gameQuestion));
            }
            return gameQuestionDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public List<GameQuestionDTO> getAllGameQuesitonByQuestionId(int questionId) throws BaseException {
        try {
            List<GameQuestion> gameQuestionList = gameQuestionRepository.findAllByQuestionId(questionId);
            if (gameQuestionList == null) throw new BaseException(BaseResponseStatus.OOPS);

            List<GameQuestionDTO> gameQuestionDTOList = new ArrayList<>();
            for (GameQuestion gameQuestion : gameQuestionList) {
                gameQuestionDTOList.add(new GameQuestionDTO(gameQuestion));
            }
            return gameQuestionDTOList;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }
}