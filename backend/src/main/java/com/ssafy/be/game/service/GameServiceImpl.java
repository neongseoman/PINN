package com.ssafy.be.game.service;

import com.ssafy.be.common.component.*;
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

    @Override
    public GameStartResponseDTO startGame(int gamerId, GameStartRequestDTO gameStartRequestDTO) throws BaseException {
        try {
            int gameId = gameStartRequestDTO.getGameId();
            ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
            GameComponent existGame = games.get(gameId);
            if (existGame == null) {
                throw new BaseException(null);
            }

            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
            if (gamerId != existGame.getLeaderId()) {
                throw new BaseException(null);
            }

            GameStartResponseDTO gameStartResponseDTO = new GameStartResponseDTO(gameStartRequestDTO.getSenderDateTime(), gameStartRequestDTO.getSenderNickname(), gameStartRequestDTO.getSenderGameId(), gameStartRequestDTO.getSenderTeamId(), gameStartRequestDTO.getCode(), gameStartRequestDTO.getMsg());
            gameStartResponseDTO.setCodeAndMsg(1111, "game start");

            // 시작 시작 기록
            existGame.setStartedTime(gameStartRequestDTO.getSenderDateTime());

            // status "start"로 변경
            if (existGame.getStatus() == GameStatus.READY) {
                existGame.setStatus(GameStatus.START);
            } else {
                throw new BaseException(null);
            }
            //

            gameStartResponseDTO.setGameId(gameStartResponseDTO.getGameId());
            return gameStartResponseDTO;
        } catch (Exception e) {
            throw new BaseException(null);
        }
    }

    @Override
    public GameInitResponseDTO findGameInfo(int gamerId, GameInitRequestDTO gameInitRequestDTO) throws BaseException {

        // TODO: 게임 시작 요청 시 테마에 해당하는 문제 랜덤 배정 + GameAndQuestion table에 insert
        // 지금은 GameAndQuestion table에 이미 문제가 배정되어 들어있다고 가정!

        int gameId = gameInitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) {
            throw new BaseException(null);
        }

        try {
            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
            if (gamerId != existGame.getLeaderId()) {
                throw new BaseException(null);
            }

            GameInitResponseDTO gameInitResponseDTO = new GameInitResponseDTO(gameInitRequestDTO.getSenderDateTime(), gameInitRequestDTO.getSenderNickname(), gameInitRequestDTO.getSenderGameId(), gameInitRequestDTO.getSenderTeamId(), gameInitRequestDTO.getCode(), gameInitRequestDTO.getMsg());
            gameInitResponseDTO.setCodeAndMsg(1112, "game init 성공");

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
    public RoundInitResponseDTO findStage1Info(int gamerId, RoundInitRequestDTO roundInitRequestDTO) throws BaseException {

        // 해당 라운드의 문제 + 스테이지1에 해당하는 힌트 return

        int gameId = roundInitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) {
            throw new BaseException(null);
        }

        try {
            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
            if (gamerId != existGame.getLeaderId()) {
                throw new BaseException(null);
            }

            RoundInitResponseDTO roundInitResponseDTO = new RoundInitResponseDTO(roundInitRequestDTO.getSenderDateTime(), roundInitRequestDTO.getSenderNickname(), roundInitRequestDTO.getSenderGameId(), roundInitRequestDTO.getSenderTeamId(), -1, null);
            roundInitResponseDTO.setCodeAndMsg(1113, "round init 성공");

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
    public Stage2InitResponseDTO findStage2Info(int gamerId, Stage2InitRequestDTO stage2InitRequestDTO) throws BaseException {

        int gameId = stage2InitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) {
            throw new BaseException(null);
        }

        try {
            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
            if (gamerId != existGame.getLeaderId()) {
                throw new BaseException(null);
            }

            Stage2InitResponseDTO stage2InitResponseDTO = new Stage2InitResponseDTO(stage2InitRequestDTO.getSenderDateTime(), stage2InitRequestDTO.getSenderNickname(), stage2InitRequestDTO.getSenderGameId(), stage2InitRequestDTO.getSenderTeamId(), stage2InitRequestDTO.getCode(), stage2InitRequestDTO.getMsg());
            stage2InitResponseDTO.setCodeAndMsg(1114, "stage2 init 성공");

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