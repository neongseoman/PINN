package com.ssafy.be.game.service;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.component.HintComponent;
import com.ssafy.be.common.component.QuestionComponent;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.repository.GameRepository;
import com.ssafy.be.game.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameManager gameManager;

    @Autowired
    private GameServiceImpl(GameRepository gameRepository, GameManager gameManager) {
        this.gameRepository = gameRepository;
        this.gameManager = gameManager;
    }

    /////
    // TODO: BaseException에 임시로 null 넣어둔 거 exception 종류에 맞게 수정

    /*
    생각을 해봐
    1. 게임 시작 요청이 옴 > db에 기본 정보 insert 하고 gameManager에 값 세팅하는 restAPI든 socket API든 있어야 함
    2. 그 후, 최초 게임 시작에 따른 로딩 시간에 initGame > 이거부터 ㄱ가정할게요...
     */

    @Override
    public GameStartResponseDTO startGame(GameStartRequestDTO gameStartRequestDTO) throws BaseException {
        GameStartResponseDTO gameStartResponseDTO = new GameStartResponseDTO();

        try {
            return gameStartResponseDTO;
        } catch (Exception e) {
            throw new BaseException(null);
        }
    }

    @Override
    public GameInitResponseDTO findGameInfo(GameInitRequestDTO gameInitRequestDTO) throws BaseException {

        // TODO: 게임 시작 요청 시 테마에 해당하는 문제 랜덤 배정 + GameAndQuestion table에 insert
        // 지금은 GameAndQuestion table에 이미 문제가 배정되어 들어있다고 가정!

        int gameId = gameInitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        System.out.println(games.toString());
        GameComponent existGame = games.get(gameId);
        if (existGame == null) {
            System.out.println("existGame이 gameManager에 없음");
            throw new BaseException(null);
        }

        // DB에서 게임 정보 조회
//        Game existGame = gameRepository.findById(gameId).orElse(null);
//        if (existGame == null) {
//            throw new BaseException(null);
//        }

        GameInitResponseDTO gameInitResponseDTO = new GameInitResponseDTO();

        try {
            // game의 정보 중 필요한 것들을 gIRD에 담아서 return
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

        // 해당 라운드의 문제 + 스테이지1에 해당하는 힌트 return
        // TODO: 권한 체크 ..

        // DB 조회 버전 :)
//        Game existGame = gameRepository.findById(roundInitRequestDTO.getGameId()).orElse(null);
//        if (existGame == null) {
//            throw new BaseException(null);
//        }
        int gameId = roundInitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) {
            throw new BaseException(null);
        }

        RoundInitResponseDTO roundInitResponseDTO = new RoundInitResponseDTO();

        try {
            int round = roundInitRequestDTO.getRound();
            QuestionComponent question = existGame.getQuestions().get(round + 1);

            // stage1 힌트만 골라내기
            List<HintComponent> hints = question.getHints();
            List<HintComponent> stage1Hints = new ArrayList<>();
            for (HintComponent hint : hints) {
                if (hint.getOfferStage() == 1) {
                    stage1Hints.add(hint);
                }
            }

            // 문제 정보 + 문제의 스테이지1 힌트 정보 담아 보내기 ..
            roundInitResponseDTO.setGameId(existGame.getGameId());
            roundInitResponseDTO.setRound(round);
            roundInitResponseDTO.setQuestionId(question.getQuestionId());
            roundInitResponseDTO.setLat(question.getLat());
            roundInitResponseDTO.setLng(question.getLng());
            roundInitResponseDTO.setHints(stage1Hints);

            return roundInitResponseDTO;
        } catch (Exception e) {
            throw new BaseException(null);
        }
    }

    @Override
    public Stage2InitResponseDTO findStage2Info(Stage2InitRequestDTO stage2InitRequestDTO) throws BaseException {
        // db 조회 버전 ..
//        Game existGame = gameRepository.findById(stage2InitRequestDTO.getGameId()).orElse(null);
//        if (existGame == null) {
//            throw new BaseException(null);
//        }

        int gameId = stage2InitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) {
            throw new BaseException(null);
        }

        Stage2InitResponseDTO stage2InitResponseDTO = new Stage2InitResponseDTO();

        try {
            int round = stage2InitRequestDTO.getRound();
            QuestionComponent question = existGame.getQuestions().get(round + 1);

            // stage2 힌트만 골라내기
            List<HintComponent> hints = question.getHints();
            List<HintComponent> stage2Hints = new ArrayList<>();
            for (HintComponent hint : hints) {
                if (hint.getOfferStage() == 2) {
                    stage2Hints.add(hint);
                }
            }

            stage2InitResponseDTO.setGameId(gameId);
            stage2InitResponseDTO.setRound(round);
            stage2InitResponseDTO.setStage(2);
            stage2InitResponseDTO.setHints(stage2Hints);

            return stage2InitResponseDTO;
        } catch (Exception e) {
            throw new BaseException(null);
        }
    }


}
