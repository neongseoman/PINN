package com.ssafy.be.game.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.Provider.ScheduleProvider;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.model.vo.*;
import com.ssafy.be.game.service.GameService;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@Log4j2
@RequiredArgsConstructor()
public class GameController {
    private final ScheduleProvider scheduleProvider;
    private final GameService gameService;
    private final SimpMessageSendingOperations sendingOperations;
    private final JwtProvider jwtProvider;

    /////
    // TODO: 한 게임에 대해 중복 요청 검증 처리 필요
    // 단순 game status 변경 + 참가자들에게 시작 소식 broadcast 하여 로딩 화면으로 넘어갈 수 있도록 함


    /*
    Socket
     */

    @MessageMapping("/game/start")
    public void startGame(GameStartRequestDTO gameStartRequestDTO, StompHeaderAccessor accessor) throws ExecutionException, InterruptedException, BaseException {
        log.debug("Is this async? Start of Method : {}", LocalDateTime.now());

        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();
        GameStartVO gameStartVO = gameService.startGame(gamerId, gameStartRequestDTO);
        GameInitVO gameInitVO = gameService.initGame(gamerId, gameStartRequestDTO);
        int gameId = gameInitVO.getGameId();
        int currentRound = 0;

        // round가 늘어난다면 이걸 늘리면 될 것 같음.

        scheduleProvider.startGame(gameInitVO.getGameId(), currentRound)
                .thenCompose(v -> {
                    // IntStream으로 라운드 수만큼 체인 생성
                    CompletableFuture<Integer> roundChain = CompletableFuture.completedFuture(currentRound);

                    // 각 라운드에 대해 체인에 비동기 작업을 연결
                    for (int round = 1; round <= gameStartRequestDTO.getRoundCount(); round++) {
                        final int currentRoundInLoop = round;
                        roundChain = roundChain.thenCompose(ignored -> scheduleProvider.roundScheduler(gameId, gameStartRequestDTO, currentRoundInLoop));

                    }

                    // 마지막 결과를 `CompletableFuture<Void>`로 변환
                    return roundChain.thenApply(ignored -> null);
                })
                .exceptionally(ex -> {
                    log.error("Error occurred in the CompletableFuture chain: ", ex);
                    throw new BaseException(BaseResponseStatus.OOPS, gameId);
                });


    }

    @MessageMapping("/team/pin") // 핀 위치 변경 시, 동일 팀원+guess 마친 팀들에게 변경한 핀 위치 broadcast
    public void movePin(PinMoveRequestDTO pinMoveRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        PinMoveVO pinMoveVO = gameService.movePin(gamerId, pinMoveRequestDTO);

        // `/team/{gameId}/{teamId}` & `/guess/{gameId}`를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/team/" + pinMoveVO.getSenderGameId() + "/" + pinMoveVO.getSenderTeamId(), pinMoveVO);
        sendingOperations.convertAndSend("/guess/" + pinMoveVO.getSenderGameId(), pinMoveVO);
    }

    @MessageMapping("/team/guess")
    public void guessPin(PinGuessRequestDTO pinGuessRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        PinGuessVO pinGuessVO = gameService.guessPin(gamerId, pinGuessRequestDTO);

        // `/team/{gameId}/{teamId}` & `/guess/{gameId}`를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/team/" + pinGuessVO.getSenderGameId() + "/" + pinGuessVO.getSenderTeamId(), pinGuessVO);
        sendingOperations.convertAndSend("/guess/" + pinGuessVO.getSenderGameId(), pinGuessVO);
    }

    @MessageMapping("/game/round/finish")
    public void finishRound(RoundFinishRequestDTO roundFinishRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        RoundFinishVO roundFinishVO = gameService.finishRound(roundFinishRequestDTO);

        // '/game/{gameId}'를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + roundFinishVO.getSenderGameId(), roundFinishVO);
    }

    @MessageMapping("/game/finish")
    public void finishGame(SocketDTO gameFinishRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        GameFinishVO gameFinishVO = gameService.finishGame(gameFinishRequestDTO);

        // '/game/{gameId}'를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + gameFinishRequestDTO.getSenderGameId(), gameFinishVO);
    }

    ////////////////////////////////////////////

    /*
    REST API
     */

    @PostMapping("/game/init")
    public BaseResponse<?> getGameInfo(@RequestBody int gameId, ServletRequest req) {
        // 요청 보낸 사용자의 gamerId
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        int gamerId = gamerPrincipalVO.getGamerId();

        GameInitVO gameInitVO = gameService.getGameInfo(gamerId, gameId);

        return new BaseResponse<>(gameInitVO);
    }

    @PostMapping("/game/round/init") // 라운드 시작(문제의 lat, lng + stage1 hint)
    public BaseResponse<?> initStage1(RoundInitRequestDTO roundInitRequestDTO, ServletRequest req) {
        // 요청 보낸 사용자의 gamerId
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        int gamerId = gamerPrincipalVO.getGamerId();

        RoundInitVO roundInitVO = gameService.findStage1Info(gamerId, roundInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
//        sendingOperations.convertAndSend("/game/" + roundInitVO.getGameId(), roundInitVO);

        return new BaseResponse<>(roundInitVO);
    }

    @PostMapping("/game/round/stage2/init") // stage2 hint
    public BaseResponse<?> initStage2(Stage2InitRequestDTO stage2InitRequestDTO, ServletRequest req) {
        // 요청 보낸 사용자의 gamerId
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        int gamerId = gamerPrincipalVO.getGamerId();

        Stage2InitVO stage2InitVO = gameService.findStage2Info(gamerId, stage2InitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
//        sendingOperations.convertAndSend("/game/" + stage2InitVO.getGameId(), stage2InitVO);

        return new BaseResponse<>(stage2InitVO);
    }

    @PostMapping("/game/round/guessed")
    public BaseResponse<?> roundGuessed(/* requestbody, */ ServletRequest req) {
        // 요청 보낸 사용자의 gamerId
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        int gamerId = gamerPrincipalVO.getGamerId();

        // RoundGuessedVO roundGuessedVO = gameService.getCurPinInfo(gamerId, ~~);

        return new BaseResponse<>(null /*roundGuessedVO*/);
    }

    @PostMapping("/game/round/result")
    public BaseResponse<?> roundResult(@RequestBody RoundResultRequestDTO roundResultRequestDTO, ServletRequest req) {
        // 요청 보낸 사용자의 gamerId
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        int gamerId = gamerPrincipalVO.getGamerId();

        RoundResultVO roundFinishVO = gameService.getRoundResult(gamerId, roundResultRequestDTO);

        return new BaseResponse<>(roundFinishVO);
    }

    @PostMapping("/game/result")
    public BaseResponse<?> gameResult(@RequestBody GameResultRequestDTO gameResultRequestDTO, ServletRequest req) {
        // 요청 보낸 사용자의 gamerId
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        int gamerId = gamerPrincipalVO.getGamerId();

        GameResultVO gameResultVO = gameService.getGameResult(gamerId, gameResultRequestDTO);

        return new BaseResponse<>(gameResultVO);
    }

}