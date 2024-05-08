package com.ssafy.be.game.service;

import com.ssafy.be.common.component.*;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.repository.GameRepository;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.game.model.domain.Hint;
import com.ssafy.be.game.model.domain.HintType;
import com.ssafy.be.game.model.domain.Question;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.model.repository.HintRepository;
import com.ssafy.be.game.model.repository.HintTypeRepository;
import com.ssafy.be.game.model.repository.QuestionRepository;
import com.ssafy.be.game.model.vo.*;
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
    public GameStartVO startGame(int gamerId, GameStartRequestDTO gameStartRequestDTO) throws BaseException {
        try {
            int gameId = gameStartRequestDTO.getGameId();
            ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();

            // 존재하는 게임인지 확인
            GameComponent existGame = games.get(gameId);
            if (existGame == null) {
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
            }
            log.info(gameId);
            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
            if (gamerId != existGame.getLeaderId()) {
                throw new BaseException(null); // TODO: exception 타입 정의
            }

            GameStartVO gameStartVO = new GameStartVO(gameStartRequestDTO.getSenderDateTime(), gameStartRequestDTO.getSenderNickname(), gameStartRequestDTO.getSenderGameId(), gameStartRequestDTO.getSenderTeamId(), gameStartRequestDTO.getCode(), gameStartRequestDTO.getMsg());
            gameStartVO.setCodeAndMsg(1111, "게임이 정상 시작되었습니다.");

            // 시작 시간 gameManager에 기록
            existGame.setStartedTime(gameStartRequestDTO.getSenderDateTime());

            // status "start"로 변경
            if (existGame.getStatus() == GameStatus.READY) {
                existGame.setStatus(GameStatus.START);
            } else {
                throw new BaseException(BaseResponseStatus.ALREADY_START_GAME);
            }

            gameStartVO.setGameId(existGame.getGameId());

//            log.info(gameStartRequestDTO);
            return gameStartVO;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new BaseException(null);
        }
    }

    @Override
    public GameInitVO initGame(int gamerId, GameStartRequestDTO gameInitRequestDTO) throws BaseException {

        // TODO: 배정한 문제 GameAndQuestion table에 insert
        // TODO: DB에 TeamGamer, Team 데이터 insert

        int gameId = gameInitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        // 존재하는 game인지 확인
        if (existGame == null) {
            throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
        }

        // 게임이 start 상태인지 확인
        if (existGame.getStatus() != GameStatus.START) {
            throw new BaseException(BaseResponseStatus.NOT_STARTED_GAME);
        }

        try {
            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
            if (gamerId != existGame.getLeaderId()) {
                throw new BaseException(null); // TODO: exception 타입 정의
            }

            GameInitVO gameInitVO = new GameInitVO(gameInitRequestDTO.getSenderDateTime(), gameInitRequestDTO.getSenderNickname(), gameInitRequestDTO.getSenderGameId(), gameInitRequestDTO.getSenderTeamId(), gameInitRequestDTO.getCode(), gameInitRequestDTO.getMsg());
            gameInitVO.setCodeAndMsg(1112, "게임 초기 설정이 정상적으로 완료되었습니다.");

            /*
             team 마다 roundCount개의 teamRounds 생성
             */
            // teamGamer를 1명 이상 보유한 경우 유효한 team으로 간주한다.
            for (int i = 1; i <= existGame.getTeamCount(); ++i) {
                TeamComponent team = existGame.getTeams().get(i);
                ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = team.getTeamGamers(); // 이게 null이라서 에러 발생 중...
                log.info(team);
                if (teamGamers != null && !teamGamers.isEmpty()) { // 유효한 팀인 경우
                    // 1. TeamRounds 생성
                    ConcurrentHashMap<Integer, TeamRoundComponent> teamRounds = new ConcurrentHashMap<>();
                    for (int j = 1; j <= existGame.getRoundCount(); ++j) {
                        TeamRoundComponent teamRound = new TeamRoundComponent();
                        teamRound.setRoundNumber(j); // roundNumber 설정
                        teamRound.setSubmitLat(-1);
                        teamRound.setSubmitLng(-1); // 최초 핀찍기 이전 위, 경도 초기값 기본 "-1"으로 설정
                        teamRound.setGuessed(false); // guessed(답 제출 여부) 기본 "false"로 설정
                        teamRounds.put(j, teamRound);
                    }
                    team.setTeamRounds(teamRounds);
                    log.info(teamRounds);

                    // TODO: 2. DB의 Team 테이블에 insert
                }
            }


            /*
             themeId 보고 question 배정
             */
            int themeId = existGame.getThemeId();
            List<Question> questionDatas = questionRepository.findByUsedAndThemeId(1, themeId); // 사용 중이고 + themeId 일치하는 것만 가져오기
            log.info("Q data : {} ",  questionDatas.size());

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
                question.setLng(questionDTO.getLng());
                question.setQuestionId(questionDTO.getQuestionId());
                question.setQuestionName(questionDTO.getQuestionName());
                question.setHints(hints);

                // 문제 완성. list에 넣기
                questions.add(question);
            }

            existGame.setQuestions(questions);
            log.info("Question Size : {}",existGame.getQuestions().size());

            // game의 정보 중 필요한 것들을 gIRD에 담아서 return
            gameInitVO.setGameId(existGame.getGameId());
            gameInitVO.setRoomName(existGame.getRoomName());
            gameInitVO.setLeaderId(existGame.getLeaderId());
            gameInitVO.setRoundCount(existGame.getRoundCount());
            gameInitVO.setThemeId(existGame.getThemeId());
            gameInitVO.setStage1Time(existGame.getStage1Time());
            gameInitVO.setStage2Time(existGame.getStage2Time());
            gameInitVO.setStartedTime(existGame.getStartedTime());

            log.info(gameInitVO);
            return gameInitVO;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new BaseException(null); // TODO: exception 타입 정의
        }
    }

    @Override
    public RoundInitVO findStage1Info(int gamerId, RoundInitRequestDTO roundInitRequestDTO) throws BaseException {

        // 해당 라운드의 문제 + 스테이지1에 해당하는 힌트 return

        int gameId = roundInitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) {
            throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
        }
        log.info(existGame.toString());

        try {
            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
            if (gamerId != existGame.getLeaderId()) {
                throw new BaseException(null);
            }

            RoundInitVO roundInitVO = new RoundInitVO(roundInitRequestDTO.getSenderDateTime(), roundInitRequestDTO.getSenderNickname(), roundInitRequestDTO.getSenderGameId(), roundInitRequestDTO.getSenderTeamId(), -1, null);
            roundInitVO.setCodeAndMsg(1113, "라운드 초기 정보를 받아오는 데에 성공했습니다.");

            int round = roundInitRequestDTO.getRound();
            log.info("{} : {}",round, existGame.getQuestions().size());
            QuestionComponent question = existGame.getQuestions().get(round - 1);

            // stage1 힌트만 골라내기
            List<HintComponent> hints = question.getHints();
            List<HintComponent> stage1Hints = new ArrayList<>();
            for (HintComponent hint : hints) {
                if (hint.getOfferStage() == 1) {
                    stage1Hints.add(hint);
                }
            }

            // 문제 정보 + 문제의 스테이지1 힌트 정보 담아 보내기 ..
            roundInitVO.setGameId(existGame.getGameId());
            roundInitVO.setRound(round);
            roundInitVO.setQuestionId(question.getQuestionId());
            roundInitVO.setQuestionName(question.getQuestionName());
            roundInitVO.setLat(question.getLat());
            roundInitVO.setLng(question.getLng());
            roundInitVO.setHints(stage1Hints);

            return roundInitVO;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(null);
        }
    }

    @Override
    public Stage2InitVO findStage2Info(int gamerId, Stage2InitRequestDTO stage2InitRequestDTO) throws BaseException {

        int gameId = stage2InitRequestDTO.getGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) {
            throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
        }

        try {
            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
            if (gamerId != existGame.getLeaderId()) {
                throw new BaseException(null);
            }

            Stage2InitVO stage2InitVO = new Stage2InitVO(stage2InitRequestDTO.getSenderDateTime(), stage2InitRequestDTO.getSenderNickname(), stage2InitRequestDTO.getSenderGameId(), stage2InitRequestDTO.getSenderTeamId(), stage2InitRequestDTO.getCode(), stage2InitRequestDTO.getMsg());
            stage2InitVO.setCodeAndMsg(1114, "stage2 추가 힌트를 받아오는 데에 성공했습니다.");

            int round = stage2InitRequestDTO.getRound();
            QuestionComponent question = existGame.getQuestions().get(round - 1);

            // stage2 힌트만 골라내기
            List<HintComponent> hints = question.getHints();
            List<HintComponent> stage2Hints = new ArrayList<>();
            for (HintComponent hint : hints) {
                if (hint.getOfferStage() == 2) {
                    stage2Hints.add(hint);
                }
            }

            stage2InitVO.setGameId(gameId);
            stage2InitVO.setRound(round);
            stage2InitVO.setStage(2);
            stage2InitVO.setHints(stage2Hints);

            return stage2InitVO;
        } catch (Exception e) {
//            e.printStackTrace();
            throw new BaseException(null);
        }
    }

    @Override
    public PinMoveVO movePin(int gamerId, PinMoveRequestDTO pinMoveRequestDTO) throws BaseException {

        int gameId = pinMoveRequestDTO.getSenderGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) { // 존재하는 게임인지 확인
            throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
        }

        // TODO: 요청자가 해당 팀의 팀원인지 확인

        // gameManager/team/teamRound에 정보 업데이트
        // TODO: team마다 teamRound를 언제 new 해서 추가해둘 것인지 결정해야 한다. > 일단 game init하는 시점으로 구현
        TeamComponent submitTeam = existGame.getTeams().get(pinMoveRequestDTO.getSenderTeamId());
        ConcurrentHashMap<Integer, TeamRoundComponent> teamRounds = submitTeam.getTeamRounds();
        if (teamRounds == null) {
            throw new BaseException(BaseResponseStatus.OOPS); // TODO: exception 타입 정의
        }
        TeamRoundComponent submitTeamRound = teamRounds.get(pinMoveRequestDTO.getRoundNumber());

        // 이미 guess한 팀 아닌지 확인
        if (submitTeamRound.isGuessed()) {
            throw new BaseException(BaseResponseStatus.ALREADY_GUESSED_TEAM);
        }
        // guess 안 했으면 제출된 정보로 submitTeamRound 업데이트
        submitTeamRound.setSubmitTime(pinMoveRequestDTO.getSenderDateTime());
        submitTeamRound.setSubmitStage(pinMoveRequestDTO.getSubmitStage());
        submitTeamRound.setSubmitLat(pinMoveRequestDTO.getSubmitLat());
        submitTeamRound.setSubmitLng(pinMoveRequestDTO.getSubmitLng());

        // 입력된 lat, lng 기반 score 계산 + submitTeamRound에 저장
        // TODO: 제출 스테이지별 점수 상한선 지정 필요
        int round = pinMoveRequestDTO.getRoundNumber();
        QuestionComponent answer = existGame.getQuestions().get(round - 1);
        int submitScore = calculateScore(answer.getLat(), answer.getLng(), pinMoveRequestDTO.getSubmitLat(), pinMoveRequestDTO.getSubmitLng()); // 점수 계산
        submitTeamRound.setRoundScore(submitScore);

        // broadcast할 정보 responseDTO에 채우기
        PinMoveVO pinMoveVO = new PinMoveVO(pinMoveRequestDTO.getSenderDateTime(), pinMoveRequestDTO.getSenderNickname(), pinMoveRequestDTO.getSenderGameId(), pinMoveRequestDTO.getSenderTeamId(), pinMoveRequestDTO.getCode(), pinMoveRequestDTO.getMsg());
        pinMoveVO.setCodeAndMsg(1115, "변경한 핀 위치가 정상 제출 및 반영되었습니다.");
        pinMoveVO.setGamerId(gamerId);
        pinMoveVO.setRoundNumber(pinMoveRequestDTO.getRoundNumber());
        pinMoveVO.setSubmitStage(pinMoveRequestDTO.getSubmitStage());
        pinMoveVO.setSubmitLat(pinMoveRequestDTO.getSubmitLat());
        pinMoveVO.setSubmitLng(pinMoveRequestDTO.getSubmitLng());

        log.info(pinMoveVO);
        return pinMoveVO;
    }

    @Override
    public PinGuessVO guessPin(int gamerId, PinGuessRequestDTO pinGuessRequestDTO) throws BaseException {

        int gameId = pinGuessRequestDTO.getSenderGameId();
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        GameComponent existGame = games.get(gameId);
        if (existGame == null) { // 존재하는 게임인지 확인
            throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
        }

        // TODO: gamerId가 senderTeamId 팀의 구성원인지 확인

        // senderTeamId 팀이 roundNumber 라운드에 한 번이라도 핀 찍은 적 있는지 확인
        TeamComponent guessTeam = existGame.getTeams().get(pinGuessRequestDTO.getSenderTeamId());
        ConcurrentHashMap<Integer, TeamRoundComponent> teamRounds = guessTeam.getTeamRounds();
        if (teamRounds == null) {
            throw new BaseException(null); // TODO: exception 타입 정의
        }
        TeamRoundComponent guessTeamRound = teamRounds.get(pinGuessRequestDTO.getRoundNumber());
        // 이미 guess한 팀 아닌지 확인
        if (guessTeamRound.isGuessed()) {
            throw new BaseException(BaseResponseStatus.ALREADY_GUESSED_TEAM);
        }

        // 해당 라운드에서 한 번도 핀 찍은 적 없으면 점수 0점 처리하기
        if (guessTeamRound.getSubmitLat() == -1 || guessTeamRound.getSubmitLng() == -1) {
            guessTeamRound.setRoundScore(0);
        }

        // senderTeamId 팀의 guessed 상태 true로 업데이트 > 중복 guess+추가 핀 찍기 방지하기
        guessTeamRound.setGuessed(true);

        // 정상적으로 guess 완료 > pinGuessVO 생성하여 정보 담아 return
        PinGuessVO pinGuessVO = new PinGuessVO(pinGuessRequestDTO.getSenderDateTime(), pinGuessRequestDTO.getSenderNickname(), pinGuessRequestDTO.getSenderGameId(), pinGuessRequestDTO.getSenderTeamId(), pinGuessRequestDTO.getCode(), pinGuessRequestDTO.getMsg());
        pinGuessVO.setCodeAndMsg(1116, "소속 팀의 현재 라운드 Guess가 완료되었습니다.");

        return pinGuessVO;

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

    // 위, 경도값 기반으로 두 지점 간의 거리(km)를 계산
    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        final int R = 6371; // 지구의 반지름 (단위: 킬로미터)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public static int calculateScore(double answerLat, double answerLng, double submitLat, double submitLng) {
        // TODO: 계산된 거리+제출 스테이지에 따른 점수 계산 로직 추가 필요
        return (int) calculateDistance(answerLat, answerLng, submitLat, submitLng);
    }

}