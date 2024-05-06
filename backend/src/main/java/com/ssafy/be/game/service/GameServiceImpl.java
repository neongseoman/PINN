package com.ssafy.be.game.service;

import com.ssafy.be.common.component.*;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.repository.GameRepository;
import com.ssafy.be.game.model.domain.Hint;
import com.ssafy.be.game.model.domain.HintType;
import com.ssafy.be.game.model.domain.Question;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.model.repository.HintRepository;
import com.ssafy.be.game.model.repository.HintTypeRepository;
import com.ssafy.be.game.model.repository.QuestionRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j2
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameManager gameManager;
    private final QuestionRepository questionRepository;
    private final HintRepository hintRepository;
    private final HintTypeRepository hintTypeRepository;

    @Autowired
    private GameServiceImpl(GameRepository gameRepository, GameManager gameManager, QuestionRepository questionRepository, HintRepository hintRepository, HintTypeRepository hintTypeRepository) {
        this.gameRepository = gameRepository;
        this.gameManager = gameManager;
        this.questionRepository = questionRepository;
        this.hintRepository = hintRepository;
        this.hintTypeRepository = hintTypeRepository;
    }

    /////
    // TODO: BaseException에 임시로 null 넣어둔 거 exception 종류에 맞게 수정

    @Override
    public GameStartResponseDTO startGame(int gamerId, GameStartRequestDTO gameStartRequestDTO) throws BaseException {
        try {
//            log.info(gameStartRequestDTO);
//            log.info(gamerId);

            int gameId = gameStartRequestDTO.getGameId();
            ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();

            // 존재하는 게임인지 확인
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

            // 시작 시간 gameManager에 기록
            existGame.setStartedTime(gameStartRequestDTO.getSenderDateTime());

            // status "start"로 변경
            if (existGame.getStatus() == GameStatus.READY) {
                existGame.setStatus(GameStatus.START);
            } else {
                throw new BaseException(null);
            }

            gameStartResponseDTO.setGameId(existGame.getGameId());

            log.info(gameStartRequestDTO);
            return gameStartResponseDTO;
        } catch (Exception e) {
            throw new BaseException(null);
        }
    }

    @Override
    public GameInitResponseDTO initGame(int gamerId, GameInitRequestDTO gameInitRequestDTO) throws BaseException {

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

            /*
             themeId 보고 question 배정
             */
            int themeId = existGame.getThemeId();
            List<Question> questionDatas = questionRepository.findByUsedAndThemeId(1, themeId); // 사용 중이고 + themeId 일치하는 것만 가져오기

            // 랜덤 (roundCount)개의 인덱스 선택
            List<Integer> randomIndices = getRandomIndices(questionDatas.size(), existGame.getRoundCount());

            // 선택된 랜덤 인덱스로 question 구성 & questionId 보고 힌트 배정
            List<QuestionComponent> questions = new ArrayList<>();
            int tmp = 1;
            for (int index : randomIndices) {
                QuestionDTO questionDTO = new QuestionDTO(questionDatas.get(index));
                QuestionComponent question = new QuestionComponent();

                // questionId 바탕으로 hint 받아와서 배정
                List<Hint> hintDatas = hintRepository.findByUsedAndQuestionId(1, questionDTO.getQuestionId());
                List<HintComponent> hints = new ArrayList<>();
                for (Hint hintData : hintDatas) {
                    // hintTypeId 바탕으로 hintTypeName 받아오기
                    HintType existHintType = hintTypeRepository.findById(hintData.getHintTypeId()).orElse(null);
                    if (existHintType == null) {
                        throw new BaseException(null); // TODO: exception 타입 정의
                    }
                    HintTypeDTO hintTypeDTO = new HintTypeDTO(existHintType);

                    // hintComponent에 정보 채우기
                    HintComponent hint = new HintComponent();
                    hint.setHintId(hintData.getHintId());
                    hint.setHintTypeId(hintData.getHintTypeId());
                    hint.setHintValue(hintData.getHintValue());
                    hint.setOfferStage(hintData.getOfferStage());
                    hint.setHintTypeName(hintTypeDTO.getHintTypeName());

                    hints.add(hint); // 다 채운 뒤 hints 리스트에 추가하기
                }

                // questionComponent에 정보 채우기
                question.setRound(tmp++);
                question.setLat(questionDTO.getLat());
                question.setLat(questionDTO.getLng());
                question.setQuestionName(questionDTO.getQuestionName());
                question.setQuestionId(questionDTO.getQuestionId());
                question.setHints(hints);
            }
            existGame.setQuestions(questions);

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

    ///////

    public static List<Integer> getRandomIndices(int maxIndex, int count) {
        List<Integer> randomIndices = new ArrayList<>();
        Random random = new Random();

        // 중복되지 않는 랜덤한 인덱스 선택
        while (randomIndices.size() < count) {
            int randomIndex = random.nextInt(maxIndex);
            if (!randomIndices.contains(randomIndex)) {
                randomIndices.add(randomIndex);
            }
        }

        return randomIndices;
    }

}