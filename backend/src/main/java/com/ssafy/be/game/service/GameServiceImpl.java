package com.ssafy.be.game.service;

import com.ssafy.be.common.component.*;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.common.model.repository.GameRepository;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.game.model.domain.*;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.model.dto.entity.*;
import com.ssafy.be.game.model.repository.*;
import com.ssafy.be.game.model.vo.*;
import com.ssafy.be.gamer.model.domain.GamerStatus;
import com.ssafy.be.gamer.model.dto.GamerLogDTO;
import com.ssafy.be.gamer.model.dto.GamerStatusDTO;
import com.ssafy.be.gamer.repository.GamerLogRepository;
import com.ssafy.be.gamer.repository.GamerStatusRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.ssafy.be.common.Provider.ColorCode.*;

@Service
@Log4j2
public class GameServiceImpl implements GameService {


    private final GameManager gameManager;

    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final TeamGamerRepository teamGamerRepository;
    private final TeamRoundRepository teamRoundRepository;

    private final GameQuestionRepository gameQuestionRepository;
    private final QuestionRepository questionRepository;
    private final HintRepository hintRepository;
    private final HintTypeRepository hintTypeRepository;

    private final GamerStatusRepository gamerStatusRepository;
    private final GamerLogRepository gamerLogRepository;

    @Autowired
    private GameServiceImpl(GameRepository gameRepository, TeamRepository teamRepository, TeamGamerRepository teamGamerRepository, TeamRoundRepository teamRoundRepository, GameQuestionRepository gameQuestionRepository, GameManager gameManager, QuestionRepository questionRepository, HintRepository hintRepository, HintTypeRepository hintTypeRepository, GamerStatusRepository gamerStatusRepository, GamerLogRepository gamerLogRepository) {
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
        this.teamGamerRepository = teamGamerRepository;
        this.teamRoundRepository = teamRoundRepository;
        this.gameQuestionRepository = gameQuestionRepository;
        this.gameManager = gameManager;
        this.questionRepository = questionRepository;
        this.hintRepository = hintRepository;
        this.hintTypeRepository = hintTypeRepository;
        this.gamerStatusRepository = gamerStatusRepository;
        this.gamerLogRepository = gamerLogRepository;

    }

    private final static int NOT_GUESSED_STAGE = 0;
    private final static int DEVELOPER_GAMER_ID = 0;
    private final static int NOT_SUBMITTED_CORD = 1000;
    private final static int MAX_ROUND_SCORE = 5000;
    private final static double STAGE2_SCORE_LIMIT_RATE = 0.7;
    private final static int[] THEME_SCORE_PENALTY = {1, 10, 10, 10, 1};
    private final static int RANDOM_THEME_ID = 1;

    /////
    // TODO: BaseException에 임시로 null 넣어둔 거 exception 종류에 맞게 수정

    @Override
    public GameStartVO startGame(int gamerId, GameStartRequestDTO gameStartRequestDTO) throws BaseException {
        try {
            int gameId = gameStartRequestDTO.getGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) {
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME, gamerId);
            }

            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
//            if (gamerId != existGame.getLeaderId()) {
//                throw new BaseException(BaseResponseStatus.OOPS);
//            }

            GameStartVO gameStartVO = new GameStartVO(gameStartRequestDTO.getSenderDateTime(), gameStartRequestDTO.getSenderNickname(), gameStartRequestDTO.getSenderGameId(), gameStartRequestDTO.getSenderTeamId(), gameStartRequestDTO.getCode(), gameStartRequestDTO.getMsg());
            gameStartVO.setCodeAndMsg(1111, "게임이 정상 시작되었습니다.");

            // 시작 시간 gameManager에 기록
            existGame.setStartedTime(gameStartRequestDTO.getSenderDateTime());

            // status "start"로 변경
            if (existGame.getStatus() == GameStatus.READY) {
                existGame.setStatus(GameStatus.START);

                // DB 상 game의 정보 업데이트!
                Game gameData = gameRepository.findById(gameId).orElse(null);
                gameData.setStartedTime(existGame.getStartedTime());
                gameData.setLeaderId(existGame.getLeaderId());
                gameData.setThemeId(existGame.getThemeId());
                gameData.setRoundCount(existGame.getRoundCount());
                gameData.setStage1Time(existGame.getStage1Time());
                gameData.setStage2Time(existGame.getStage2Time());
                gameRepository.save(gameData);

            } else {
                throw new BaseException(BaseResponseStatus.ALREADY_START_GAME, gamerId);
            }

            gameStartVO.setGameId(existGame.getGameId());

//            log.info(gameStartRequestDTO);
            return gameStartVO;
        } catch (BaseException e) {
//            log.error("Socket Error");
            e.printStackTrace();
            throw new BaseException(e.getStatus(), gamerId); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS, gamerId);
        }
    }

    @Override
    public GameInitVO initGame(int gamerId, GameStartRequestDTO gameInitRequestDTO) throws BaseException {
        try {
            int gameId = gameInitRequestDTO.getGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            // 존재하는 game인지 확인
            if (existGame == null) {
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME, gamerId);
            }

            // 게임이 start 상태인지 확인
            if (existGame.getStatus() != GameStatus.START) {
                throw new BaseException(BaseResponseStatus.NOT_STARTED_GAME, gamerId);
            }

            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
//            if (gamerId != existGame.getLeaderId()) {
//                throw new BaseException(BaseResponseStatus.OOPS);
//            }

            /*
             team 마다 roundCount개의 teamRounds 생성
             &
             team, teamGamer 정보들 DB에 insert
             */
            // teamGamer를 1명 이상 보유한 경우 유효한 team으로 간주한다.
            for (int i = 1; i <= existGame.getTeamCount(); ++i) {
                TeamComponent team = existGame.getTeams().get(i);
                ConcurrentHashMap<Integer, TeamGamerComponent> teamGamers = team.getTeamGamers();
//                log.info(team);
                if (teamGamers != null && !teamGamers.isEmpty()) { // 유효한 팀인 경우
                    // 1. TeamRounds 생성
                    ConcurrentHashMap<Integer, TeamRoundComponent> teamRounds = new ConcurrentHashMap<>();
                    for (int j = 1; j <= existGame.getRoundCount(); ++j) {
                        TeamRoundComponent teamRound = new TeamRoundComponent();
                        teamRound.setTeamId(team.getTeamId());
                        teamRound.setColorCode(team.getColorCode());
                        teamRound.setRoundNumber(j); // roundNumber 설정
                        teamRound.setSubmitLat(NOT_SUBMITTED_CORD);
                        teamRound.setSubmitLng(NOT_SUBMITTED_CORD); // 최초 핀찍기 이전 위, 경도 초기값 기본값으로 설정
                        teamRound.setGuessed(false); // guessed(답 제출 여부) 기본 "false"로 설정
                        teamRounds.put(j, teamRound);
                    }
                    team.setTeamRounds(teamRounds);
//                    log.info(teamRounds);

                    // 2. DB의 Team 테이블에 insert
                    TeamDTO teamDTO = new TeamDTO();
                    teamDTO.setGameId(gameId);
                    teamDTO.setColorId(team.getTeamNumber());
                    teamDTO.setTeamNumber(team.getTeamNumber());
                    teamDTO.setReady(team.isReady());
                    teamDTO.setLastReadyTime(gameInitRequestDTO.getSenderDateTime());
                    teamDTO.setFinalRank(team.getFinalRank());
                    teamDTO.setFinalScore(team.getFinalScore());

                    int savedTeamId = teamRepository.save(teamDTO.toEntity()).getTeamId(); // DB에 insert
                    team.setTeamNumber(savedTeamId); // 일단 teamNumber 에 넣어둠

                    // 3. DB의 TeamGamer 테이블에 해당 팀의 teamGamer들 insert
                    for (TeamGamerComponent teamGamer : teamGamers.values()) {
                        TeamGamerDTO teamGamerDTO = new TeamGamerDTO();
                        // teamGamerNumber에 따라 color id 저장
                        if (teamGamer.getTeamGamerNumber() == 1) {
                            teamGamerDTO.setColorId(NEON_GREEN.getTeamNumber());
                        } else if (teamGamer.getTeamGamerNumber() == 2) {
                            teamGamerDTO.setColorId(NEON_PINK.getTeamNumber());
                        } else {
                            teamGamerDTO.setColorId(NEON_YELLOW.getTeamNumber());
                        }
                        teamGamerDTO.setTeamId(team.getTeamNumber());
                        teamGamerDTO.setGamerId(teamGamer.getGamerId());

                        teamGamerRepository.save(teamGamerDTO.toEntity()); // DB에 teamGamer 정보 insert
                    }

                }
            }


            /*
             themeId 보고 question 배정
             */
            int themeId = existGame.getThemeId();

            List<Question> questionDatas;
            if (themeId == RANDOM_THEME_ID) {
                questionDatas = questionRepository.findByUsed(1); // '랜덤' 테마인 경우 모든 문제셋을 가져오기
            } else {
                questionDatas = questionRepository.findByUsedAndThemeId(1, themeId); // 사용 중이고 + themeId 일치하는 것만 가져오기
            }

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
                        throw new BaseException(BaseResponseStatus.OOPS, gamerId); // TODO: exception 타입 정의
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

                // 배정된 문제 정보 DB의 GameQuestion 테이블에도 insert
                GameQuestionDTO gameQuestionDTO = new GameQuestionDTO();
                gameQuestionDTO.setQuestionId(question.getQuestionId());
                gameQuestionDTO.setGameId(gameId);
                gameQuestionDTO.setRoundNumber(question.getRound());
                gameQuestionRepository.save(gameQuestionDTO.toEntity());
            }
            // gameComponent의 questions 최종 결정!
            existGame.setQuestions(questions);

            /*
             game의 라운드별 결과 저장할 roundResults 리스트 new 해서 생성
             */
            existGame.setRoundResults(new LinkedList<>());

            // TODO: 필요 없는 작업. 리팩터링 필요.
            GameInitVO gameInitVO = new GameInitVO();
            // game의 정보 중 필요한 것들을 gIRD에 담아서 return
            gameInitVO.setGameId(existGame.getGameId());
            gameInitVO.setRoomName(existGame.getRoomName());
            gameInitVO.setLeaderId(existGame.getLeaderId());
            gameInitVO.setRoundCount(existGame.getRoundCount());
            gameInitVO.setThemeId(existGame.getThemeId());
            gameInitVO.setStage1Time(existGame.getStage1Time());
            gameInitVO.setStage2Time(existGame.getStage2Time());
            gameInitVO.setStartedTime(existGame.getStartedTime());
            return gameInitVO;

        } catch (BaseException e) {
//            log.error("Socket Error");
            e.printStackTrace();
            throw new BaseException(e.getStatus(), gamerId); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS, gamerId);
        }
    }

    @Override
    public PinMoveVO movePin(int gamerId, PinMoveRequestDTO pinMoveRequestDTO) throws BaseException { // code: 1115
        log.info(pinMoveRequestDTO);

        try {
            int gameId = pinMoveRequestDTO.getSenderGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) { // 존재하는 게임인지 확인
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME, gamerId);
            }

            // TODO: 요청자가 해당 팀의 팀원인지 확인

            // gameManager/team/teamRound에 정보 업데이트
            ConcurrentHashMap<Integer, TeamRoundComponent> teamRounds = existGame.getTeams().get(pinMoveRequestDTO.getSenderTeamId()).getTeamRounds();
            if (teamRounds == null) {
                throw new BaseException(BaseResponseStatus.OOPS, gamerId); // TODO: exception 타입 정의
            }
            TeamRoundComponent submitTeamRound = teamRounds.get(pinMoveRequestDTO.getRoundNumber());

            // 이미 guess한 팀 아닌지 확인
            if (submitTeamRound.isGuessed()) {
                throw new BaseException(BaseResponseStatus.ALREADY_GUESSED_TEAM, gamerId);
            }
            // guess 안 했으면 제출된 정보로 submitTeamRound 업데이트
            submitTeamRound.setSubmitTime(pinMoveRequestDTO.getSenderDateTime());
            submitTeamRound.setSubmitStage(pinMoveRequestDTO.getSubmitStage());
            submitTeamRound.setSubmitLat(pinMoveRequestDTO.getSubmitLat());
            submitTeamRound.setSubmitLng(pinMoveRequestDTO.getSubmitLng());

            // 입력된 lat, lng 기반 score 계산 + submitTeamRound에 저장

            int round = pinMoveRequestDTO.getRoundNumber();
            QuestionComponent answer = existGame.getQuestions().get(round - 1);
            int submitScore = calculateScore(existGame.getThemeId(), answer.getLat(), answer.getLng(), pinMoveRequestDTO.getSubmitLat(), pinMoveRequestDTO.getSubmitLng()); // 점수 계산
            if (pinMoveRequestDTO.getSubmitStage() == 2) { // stage2에 찍은 핀인 경우: 산정된 점수 * STAGE2_SCORE_LIMIT_RATE 만큼만 획득 가능
                submitScore *= STAGE2_SCORE_LIMIT_RATE;
            }
            submitTeamRound.setRoundScore(submitScore);

            // broadcast할 정보 responseDTO에 채우기
            PinMoveVO pinMoveVO = new PinMoveVO(pinMoveRequestDTO.getSenderDateTime(), pinMoveRequestDTO.getSenderNickname(), pinMoveRequestDTO.getSenderGameId(), pinMoveRequestDTO.getSenderTeamId(), pinMoveRequestDTO.getCode(), pinMoveRequestDTO.getMsg());
            pinMoveVO.setCodeAndMsg(1115, "변경한 핀 위치가 정상 제출 및 반영되었습니다.");
            pinMoveVO.setGamerId(gamerId);
            pinMoveVO.setColorCode(submitTeamRound.getColorCode());
            pinMoveVO.setRoundNumber(pinMoveRequestDTO.getRoundNumber());
            pinMoveVO.setSubmitStage(pinMoveRequestDTO.getSubmitStage());
            pinMoveVO.setSubmitLat(pinMoveRequestDTO.getSubmitLat());
            pinMoveVO.setSubmitLng(pinMoveRequestDTO.getSubmitLng());

            log.info(pinMoveVO);
            return pinMoveVO;
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus(), gamerId); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS, gamerId);
        }
    }

    @Override
    public PinGuessVO guessPin(int gamerId, PinGuessRequestDTO pinGuessRequestDTO) throws BaseException { // code: 1116
        log.info(pinGuessRequestDTO);

        try {
            int gameId = pinGuessRequestDTO.getSenderGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) { // 존재하는 게임인지 확인
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME, gamerId);
            }

            // TODO: gamerId가 senderTeamId 팀의 구성원인지 확인

            // senderTeamId 팀이 roundNumber 라운드에 한 번이라도 핀 찍은 적 있는지 확인
            ConcurrentHashMap<Integer, TeamRoundComponent> teamRounds = existGame.getTeams().get(pinGuessRequestDTO.getSenderTeamId()).getTeamRounds();
            if (teamRounds == null) {
                throw new BaseException(BaseResponseStatus.OOPS, gamerId); // TODO: exception 타입 정의
            }
            TeamRoundComponent guessTeamRound = teamRounds.get(pinGuessRequestDTO.getRoundNumber());
            // 이미 guess한 팀 아닌지 확인
            if (guessTeamRound.isGuessed()) {
                throw new BaseException(BaseResponseStatus.ALREADY_GUESSED_TEAM, gamerId);
            }

            // 해당 라운드에서 한 번도 핀 찍은 적 없으면 점수 0점 처리하기
            if (guessTeamRound.getSubmitLat() == NOT_SUBMITTED_CORD || guessTeamRound.getSubmitLng() == NOT_SUBMITTED_CORD) {
                guessTeamRound.setRoundScore(0);
            }

            // senderTeamId 팀의 guessed 상태 true로 업데이트 & submitTime과 submitStage 최종 업데이트
            guessTeamRound.setGuessed(true);
            guessTeamRound.setSubmitTime(pinGuessRequestDTO.getSenderDateTime()); // 이게 guess한 시간이 됨
            guessTeamRound.setSubmitStage(pinGuessRequestDTO.getGuessStage()); // 이게 guess한 stage가 됨

            // 정상적으로 guess 완료 > pinGuessVO 생성하여 정보 담아 return
            PinGuessVO pinGuessVO = new PinGuessVO(pinGuessRequestDTO.getSenderDateTime(), pinGuessRequestDTO.getSenderNickname(), pinGuessRequestDTO.getSenderGameId(), pinGuessRequestDTO.getSenderTeamId(), pinGuessRequestDTO.getCode(), pinGuessRequestDTO.getMsg());
            pinGuessVO.setCodeAndMsg(1116, "소속 팀의 현재 라운드 Guess가 완료되었습니다.");

            log.info(pinGuessVO);
            return pinGuessVO;
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus(), gamerId); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS, gamerId);
        }

    }

    /*
    각 팀의 teamRound 최종 업데이트해서 <라운드 결과> 만듦
    & RoundFinishVO 만들어서 GameManager에 담음
     */
    @Override
    public void finishRound(RoundFinishRequestDTO roundFinishRequestDTO) throws BaseException { // code: 1117
        try {
            int gameId = roundFinishRequestDTO.getSenderGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) { // 존재하는 게임인지 확인
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME, DEVELOPER_GAMER_ID);
            }

            // 모든 팀 정보 받아오기
            ConcurrentHashMap<Integer, TeamComponent> teams = existGame.getTeams();

            // 현 라운드 결과 담을 teamRoundResults 생성
            List<TeamRoundComponent> teamRoundResults = new ArrayList<>();

            // 팀별로 TeamRoundComponent 완성해서 teamRoundResults에 채우기
            for (int i = 1; i <= existGame.getTeamCount(); ++i) {
                TeamComponent team = teams.get(i);
                if (team.getTeamGamers() == null || team.getTeamGamers().isEmpty()) { // teamGamers가 없거나 0명인 경우, 유효하지 않은 팀으로 간주
                    continue; // 패스!
                }

                // i팀의 현재 라운드 최신 핀 위치 정보+라운드 결과 저장할 teamRound 받아오기
                TeamRoundComponent teamRound = team.getTeamRounds().get(roundFinishRequestDTO.getRoundNumber());

                // TRR VO 채우기
                if (!teamRound.isGuessed()) { // guess 안 한 팀인 경우
                    // teamRound의 submitTime, submitStage를 '라운드 종료' 시점으로 업데이트
                    teamRound.setSubmitTime(roundFinishRequestDTO.getSenderDateTime());
                    teamRound.setSubmitStage(NOT_GUESSED_STAGE); // TODO: guess 안 한 팀의 submitStage 어떻게 처리할 것인지 결정해야 함

                    if (teamRound.getSubmitLat() == NOT_SUBMITTED_CORD || teamRound.getSubmitLng() == NOT_SUBMITTED_CORD) { // 핀 한 번도 안 찍고 guess도 안 한 팀인 경우
                        teamRound.setRoundScore(0); // 0점 부여
                    }
                }
                team.setFinalScore(team.getFinalScore() + teamRound.getRoundScore()); // team의 획득 총점 업데이트
                teamRound.setTotalScore(team.getFinalScore()); // 현 라운드까지의 총점 업데이트
                // 나머지 값은 그대로임.

                // 다 채운 TR을 teamRoundResults 리스트에 추가
                teamRoundResults.add(teamRound);
            }

            // 다 채운 TRRs 리스트를 roundScore 기준으로 정렬 (현 랭킹)
            Collections.sort(teamRoundResults);
            // roundScore 정렬 기준으로 각 teamRoundResult의 roundRank 채우기
            for (int i = 1; i <= teamRoundResults.size(); ++i) {
                teamRoundResults.get(i - 1).setRoundRank(i);
            }

            // totalScore 기준으로 정렬 (종합 랭킹)
            sortByTotalScore(teamRoundResults);
            // totalScore 정렬 기준으로 각 teamRoundResult의 totalRank 채우기
            for (int i = 1; i <= teamRoundResults.size(); ++i) {
                teamRoundResults.get(i - 1).setTotalRank(i);
            }

            // GameManager의 gameComponent의 roundResults에 teamRoundResults 담아서 gm이 라운드 결과 들고 있게 하기!
            existGame.getRoundResults().add(teamRoundResults);

            // RF VO에 TRRs 담아서 리턴
//            RoundFinishVO roundFinishVO = new RoundFinishVO(roundFinishRequestDTO.getSenderNickname(), roundFinishRequestDTO.getSenderGameId(), roundFinishRequestDTO.getSenderTeamId(), teamRoundResults);
//            roundFinishVO.setCodeAndMsg(1117, roundFinishRequestDTO.getRoundNumber() + "라운드 결과가 정상 계산되었습니다.");
//            log.info(roundFinishVO);
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus(), DEVELOPER_GAMER_ID); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS, DEVELOPER_GAMER_ID);
        }


    }

    @Override
    public void finishGame(SocketDTO gameFinishRequestDTO) throws BaseException { // code: 1118
        log.info(gameFinishRequestDTO);

        try {

            int gameId = gameFinishRequestDTO.getSenderGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) { // 존재하는 게임인지 확인
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
            }

            /*
            게임 결과 집계 & DB에 게임 정보 저장
             */

            // finishedTime 기록
            existGame.setFinishedTime(gameFinishRequestDTO.getSenderDateTime());
            // DB 업데이트: game의 finished_time 업데이트
            Game gameData = gameRepository.findById(gameId).orElse(null);
            if (gameData != null) {
                gameData.setFinishedTime(gameFinishRequestDTO.getSenderDateTime());
//                gameData.setFinishedTime(LocalDateTime.now());
                gameRepository.save(gameData);
            }

            // 각 teamComponent의 finalRank, finalScore 업데이트
            ConcurrentHashMap<Integer, TeamComponent> teams = existGame.getTeams();
            int roundCount = existGame.getRoundCount(); // 이 게임의 전체 라운드 수
            for (int i = 1; i <= existGame.getTeamCount(); ++i) {
                TeamComponent team = teams.get(i);
                if (team.getTeamGamers() == null || team.getTeamGamers().isEmpty()) { // teamGamers가 없거나 0명인 경우, 유효하지 않은 팀으로 간주
                    continue; // 패스
                }
                // 각 팀의 finalRank = 해당 팀의 '마지막 라운드' 기준 '총점' 등수
                // 각 팀의 finalScore = '마지막 라운드' 결과 집계 시에 이미 update 완료된 상태 .. 라고 가정 > 따로 업데이트하지 않음
                team.setFinalRank(team.getTeamRounds().get(roundCount).getTotalRank());

                // 모든 팀: team 테이블에 final_score, final_rank 업데이트
                Team teamData = teamRepository.findById(team.getTeamNumber()).orElse(null);
                if (teamData != null) {
                    teamData.setFinalScore(team.getFinalScore());
                    teamData.setFinalRank(team.getFinalRank());
                    teamRepository.save(teamData);
                }

                // 모든 팀: team_round 테이블에 insert
                for (TeamRoundComponent teamRound : team.getTeamRounds().values()) {
                    TeamRoundDTO teamRoundDTO = new TeamRoundDTO();
                    teamRoundDTO.setTeamId(team.getTeamNumber());
                    teamRoundDTO.setRoundNumber(teamRound.getRoundNumber());
                    teamRoundDTO.setRoundScore(teamRound.getRoundScore());
                    teamRoundDTO.setSubmitStage(teamRound.getSubmitStage());
                    teamRoundDTO.setSubmitTime(teamRound.getSubmitTime());
                    teamRoundDTO.setSubmitLat(teamRound.getSubmitLat()); // 미제출한 경우 NOT_SUBMITTED_CORD 값으로 들어감
                    teamRoundDTO.setSubmitLng(teamRound.getSubmitLng()); // 미제출한 경우 NOT_SUBMITTED_CORD 값으로 들어감
                    teamRoundRepository.save(teamRoundDTO.toEntity());
                }

                for (TeamGamerComponent teamGamer : team.getTeamGamers().values()) {
                    // 모든 플레이어: gamer의 gamer_status 테이블 insert OR update
                    GamerStatus existGamerStatus = gamerStatusRepository.findById(teamGamer.getGamerId()).orElse(null);
                    if (existGamerStatus == null) { // 최초 플레이인 경우: insert
                        GamerStatusDTO gamerStatusDTO = new GamerStatusDTO();
                        gamerStatusDTO.setGamerId(teamGamer.getGamerId());
                        gamerStatusDTO.setPlayCount(1);
                        if (team.getFinalRank() == 1) { // 1등한 팀의 구성원인 경우
                            gamerStatusDTO.setWinCount(1);
                        } else { // 2~10등 팀의 구성원인 경우
                            gamerStatusDTO.setWinCount(0);
                        }
                        gamerStatusRepository.save(gamerStatusDTO.toEntity());
                    } else { // 플레이 이력 있는 경우: update
                        existGamerStatus.setPlayCount(existGamerStatus.getPlayCount() + 1);
                        if (team.getFinalRank() == 1) { // 1등한 팀의 구성원인 경우에만 wincount++
                            existGamerStatus.setWinCount(existGamerStatus.getWinCount() + 1);
                        }
                        gamerStatusRepository.save(existGamerStatus);
                    }

                    // 모든 플레이어: gamer_log 테이블에 insert
                    GamerLogDTO gamerLogDTO = new GamerLogDTO();
                    gamerLogDTO.setGamerId(teamGamer.getGamerId());
                    gamerLogDTO.setGameId(team.getGameId());
                    gamerLogDTO.setTeamId(team.getTeamNumber());
                    gamerLogDTO.setRank(team.getFinalRank());
                    gamerLogDTO.setTeamColor(team.getColorCode());
                    if (existGame.getLeaderId() == teamGamer.getGamerId()) {
                        gamerLogDTO.setIsRoomLeader(1);
                    } else {
                        gamerLogDTO.setIsRoomLeader(0);
                    }
                    gamerLogDTO.setIsTeamLeader(0); // TODO: 일단 무조건 0으로 넣어~
                    gamerLogRepository.save(gamerLogDTO.toEntity());
                }
            }

        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus(), DEVELOPER_GAMER_ID); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS, DEVELOPER_GAMER_ID);
        }
    }

    //////////////////////////////////////

    @Override
    public GameInitVO getGameInfo(int gamerId, int gameId) throws BaseException {
        try {

            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) { // 존재하는 게임인지 확인
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
            }

            // TODO: gamerId가 gameId에 해당하는 게임에 속한 사용자인지 확인

            // game의 정보 중 필요한 것들을 GI VO에 담아서 return
            GameInitVO gameInitVO = new GameInitVO();
            gameInitVO.setGameId(existGame.getGameId());
            gameInitVO.setRoomName(existGame.getRoomName());
            gameInitVO.setLeaderId(existGame.getLeaderId()); // 방장 gamerId가 필요한가?
            gameInitVO.setRoundCount(existGame.getRoundCount());
            gameInitVO.setThemeId(existGame.getThemeId());
            gameInitVO.setStage1Time(existGame.getStage1Time());
            gameInitVO.setStage2Time(existGame.getStage2Time());
            gameInitVO.setStartedTime(existGame.getStartedTime());

            log.info(gameInitVO);
            return gameInitVO;
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public RoundInitVO findStage1Info(int gamerId, RoundRequestDTO roundRequestDTO) throws BaseException {

        try {
            // 해당 라운드의 문제 + 스테이지1에 해당하는 힌트 return

            int gameId = roundRequestDTO.getGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) {
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
            }

            // TODO: 요청 보낸 gamerId 사용자가 game에 속해 있는지 확인
            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
//            if (gamerId != existGame.getLeaderId()) {
//                throw new BaseException(BaseResponseStatus.OOPS);
//            }

            int round = roundRequestDTO.getRound();
            QuestionComponent question = existGame.getQuestions().get(round - 1);

            // stage1 힌트만 골라내기
            List<HintComponent> stage1Hints = new ArrayList<>();
            for (HintComponent hint : question.getHints()) {
                if (hint.getOfferStage() == 1) {
                    stage1Hints.add(hint);
                }
            }

//            RoundInitVO roundInitVO = new RoundInitVO(roundRequestDTO.getSenderDateTime(), roundRequestDTO.getSenderNickname(), roundRequestDTO.getSenderGameId(), roundRequestDTO.getSenderTeamId(), -1, null);
//            roundInitVO.setCodeAndMsg(1113, "라운드 초기 정보를 받아오는 데에 성공했습니다.");

            RoundInitVO roundInitVO = new RoundInitVO();
            // 문제 정보 + 문제의 스테이지1 힌트 정보 담아 보내기 ..
            roundInitVO.setGameId(existGame.getGameId());
            roundInitVO.setRound(round);
            roundInitVO.setQuestionId(question.getQuestionId());
            roundInitVO.setQuestionName(question.getQuestionName());
            roundInitVO.setLat(question.getLat());
            roundInitVO.setLng(question.getLng());
            roundInitVO.setHints(stage1Hints);

            log.info(roundInitVO);
            return roundInitVO;
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus()); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public Stage2InitVO findStage2Info(int gamerId, RoundRequestDTO stage2InitRequestDTO) throws BaseException {

        try {
            int gameId = stage2InitRequestDTO.getGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) {
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
            }

            // TODO: 요청 보낸 사용자가 game에 속해 있는지 확인

            // 요청 보낸 gamer_id가 GM/game의 leader_id와 일치하는지 확인
//            if (gamerId != existGame.getLeaderId()) {
//                throw new BaseException(BaseResponseStatus.OOPS); // TODO: EXCEPTION TYPE 정의
//            }

//            Stage2InitVO stage2InitVO = new Stage2InitVO(stage2InitRequestDTO.getSenderDateTime(), stage2InitRequestDTO.getSenderNickname(), stage2InitRequestDTO.getSenderGameId(), stage2InitRequestDTO.getSenderTeamId(), stage2InitRequestDTO.getCode(), stage2InitRequestDTO.getMsg());
//            stage2InitVO.setCodeAndMsg(1114, "stage2 추가 힌트를 받아오는 데에 성공했습니다.");
            Stage2InitVO stage2InitVO = new Stage2InitVO();

            int round = stage2InitRequestDTO.getRound();
            QuestionComponent question = existGame.getQuestions().get(round - 1);

            // stage2 힌트만 골라내기
//            List<HintComponent> stage2Hints = new ArrayList<>();
//            for (HintComponent hint : question.getHints()) {
//                if (hint.getOfferStage() == 2) {
//                    stage2Hints.add(hint);
//                }
//            }

            stage2InitVO.setGameId(gameId);
            stage2InitVO.setRound(round);
            stage2InitVO.setStage(2);
            stage2InitVO.setHints(question.getHints());

//            log.info(stage2InitVO); // 로그 너무 많아~!
            return stage2InitVO;
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus()); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public RoundGuessedVO getCurPinsInfo(int gamerId, RoundRequestDTO roundGuessedRequestDTO) throws BaseException {
        try {
            int gameId = roundGuessedRequestDTO.getGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) {
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
            }

            // TODO: gamerId가 속한 team이 해당 라운드에 guessed 상태인지 확인

            RoundGuessedVO roundGuessedVO = new RoundGuessedVO();
            List<TeamPinDTO> teamPins = new ArrayList<>();

            // 모든 팀의 teamRound 돌면서 RoundGuessedVO의 TeamPinDTO 채우기
            for (TeamComponent team : existGame.getTeams().values()) {
                if (team.getTeamGamers() == null || team.getTeamGamers().isEmpty()) { // teamGamers가 없거나 0명인 경우, 유효하지 않은 팀으로 간주
                    continue; // 패스
                }
                // 유효한 팀인 경우 teamRound 접근해서 RG VO 채우기
                TeamRoundComponent teamRound = team.getTeamRounds().get(roundGuessedRequestDTO.getRound());
                // 핀 찍은 적 없는 팀인 경우: submitLat, submitLng가 NOT_SUBMITTED_CORD 값으로 들어감
                teamPins.add(new TeamPinDTO(team.getTeamId(), team.getColorCode(), teamRound.isGuessed(), teamRound.getSubmitLat(), teamRound.getSubmitLng()));
            }

            roundGuessedVO.setGameId(roundGuessedRequestDTO.getGameId());
            roundGuessedVO.setTeamPins(teamPins);

            log.info(roundGuessedVO);
            return roundGuessedVO;

        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public RoundResultVO getRoundResult(int gamerId, RoundRequestDTO roundResultRequestDTO) throws BaseException {
        try {
            int gameId = roundResultRequestDTO.getGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) { // 존재하는 게임인지 확인
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
            }

            // TODO: gamerId가 game 구성원인지 확인

            List<TeamRoundComponent> roundResult = existGame.getRoundResults().get(roundResultRequestDTO.getRound() - 1);

            RoundResultVO roundResultVO = new RoundResultVO();
            roundResultVO.setGameId(roundResultRequestDTO.getGameId());
            roundResultVO.setRoundNumber(roundResultRequestDTO.getRound());
            roundResultVO.setQuestion(existGame.getQuestions().get(roundResultRequestDTO.getRound() - 1));
            roundResultVO.setRoundResult(roundResult);

            log.info(roundResultVO);
            return roundResultVO;
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus()); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }

    @Override
    public GameResultVO getGameResult(int gamerId, GameResultRequestDTO gameResultRequestDTO) throws BaseException {
        try {
            int gameId = gameResultRequestDTO.getGameId();
            GameComponent existGame = gameManager.getGames().get(gameId);
            if (existGame == null) { // 존재하는 게임인지 확인
                throw new BaseException(BaseResponseStatus.NOT_EXIST_GAME);
            }

            GameResultVO gameResultVO = new GameResultVO();
            gameResultVO.setGameId(gameResultRequestDTO.getGameId());
            gameResultVO.setTeamId(gameResultRequestDTO.getTeamId());
            gameResultVO.setRoundResults(existGame.getRoundResults());
            gameResultVO.setQuestions(existGame.getQuestions());
//            gameResultVO.setGameResult();

            log.info(gameResultVO);
            return gameResultVO;
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(e.getStatus()); // Socket에도 던지고 싶다면 GamerID를 주세요.
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.OOPS);
        }
    }


////////////

    // List<TeamRoundComponent> 를 totalScore 기준으로 내림차순 정렬
    private static void sortByTotalScore(List<TeamRoundComponent> teamRoundResults) {
        teamRoundResults.sort((o1, o2) -> {
            return o2.getTotalScore() - o1.getTotalScore(); // totalScore를 기준으로 내림차순으로 정렬
        });
    }

    // 0 ~ maxIndex 중 count 개의 숫자를 List로 반환
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

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // 'answer 위치'와 'submit 위치' 사이의 거리 기반 score 계산
    public static int calculateScore(int themeId, double answerLat, double answerLng, double submitLat, double submitLng) {
        // 최대 점수 - (정답과 핀 사이 거리) * (해당 테마의 distPenalty)
        double dist = calculateDistance(answerLat, answerLng, submitLat, submitLng);
        int scorePenalty = THEME_SCORE_PENALTY[themeId - 1];

        int penalty = (int) (dist * scorePenalty);

        return Math.max(MAX_ROUND_SCORE - penalty, 0);
    }

}