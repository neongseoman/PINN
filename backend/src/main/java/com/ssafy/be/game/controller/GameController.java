package com.ssafy.be.game.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.Provider.ScheduleProvider;
import com.ssafy.be.common.model.dto.ServerEvent;
import com.ssafy.be.common.model.dto.ServerSendEvent;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.model.vo.*;
import com.ssafy.be.game.service.GameService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

@RestController
@Log4j2
@RequestMapping("/game")
public class GameController {
    private final ScheduleProvider scheduleProvider;
    private final GameService gameService;
    private final SimpMessageSendingOperations sendingOperations;
    private final JwtProvider jwtProvider;

    @Autowired
    private GameController(ScheduleProvider scheduleProvider, GameService gameService, SimpMessageSendingOperations sendingOperations, JwtProvider jwtProvider) {
        this.scheduleProvider = scheduleProvider;
        this.gameService = gameService;
        this.sendingOperations = sendingOperations;
        this.jwtProvider = jwtProvider;
    }

    /////
    // TODO: 한 게임에 대해 중복 요청 검증 처리 필요

    @MessageMapping("/game/start") // 단순 game status 변경 + 참가자들에게 시작 소식 broadcast 하여 로딩 화면으로 넘어갈 수 있도록 함
    public void startGame(GameStartRequestDTO gameStartRequestDTO, StompHeaderAccessor accessor) throws ExecutionException, InterruptedException {
        log.info("Is this async? Start of Method : {}", LocalDateTime.now());
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();
        GameStartVO gameStartVO = gameService.startGame(gamerId, gameStartRequestDTO);
        int gameId = gameStartVO.getGameId();
        scheduleProvider.startGame(gameId)
                .thenCompose((result) -> {
                    log.info("{} : Starting gameId : {} at {}",result, gameId, LocalDateTime.now());
                    ServerSendEvent serverMsg = new ServerSendEvent(ServerEvent.START);
                    sendingOperations.convertAndSend("/game/"+result,serverMsg);
                    return scheduleProvider.roundStart(result,5);
                }).thenCompose((result)->{
                    log.info("{} : round start: {}  at {}",result, gameId, LocalDateTime.now());
                    ServerSendEvent serverMsg = new ServerSendEvent(ServerEvent.ROUND_START);
                    sendingOperations.convertAndSend("/game/"+result,serverMsg);
                    return scheduleProvider.sendHint(result,5);
                }).thenCompose((result) ->{
                    log.info("{} : Hint send: {}  at {}",result, gameId, LocalDateTime.now());
                    ServerSendEvent serverMsg = new ServerSendEvent(ServerEvent.HINT);
                    sendingOperations.convertAndSend("/game/"+result,serverMsg);
                    return scheduleProvider.roundEnd(result,5);
                }).exceptionally(ex -> {
                    log.error("Error occurred in the CompletableFuture chain: ", ex);
                    return null;
                });

//        sendingOperations.convertAndSend("/game/"+result,new ServerSendEvent(ServerEvent.START));
        log.info("Is this async? End of Method : {}", LocalDateTime.now());

        sendingOperations.convertAndSend("/game/" + gameStartVO.getGameId(), gameStartVO);
    }

    @MessageMapping("/game/init") // 게임 문제 배정 + 게임 초기 정보값 broadcast
    public void initGame(GameInitRequestDTO gameInitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();
        GameInitVO gameInitVO = gameService.initGame(gamerId, gameInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + gameInitVO.getGameId(), gameInitVO);
    }

    @MessageMapping("/game/round/init") // 라운드 시작(문제의 lat, lng + stage1 hint broadcast)
    public void initStage1(RoundInitRequestDTO roundInitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        RoundInitVO roundInitVO = gameService.findStage1Info(gamerId, roundInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + roundInitVO.getGameId(), roundInitVO);
    }

    @MessageMapping("/game/round/stage2/init") // stage2 hint broadcast
    public void initStage2(Stage2InitRequestDTO stage2InitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        Stage2InitVO stage2InitVO = gameService.findStage2Info(gamerId, stage2InitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + stage2InitVO.getGameId(), stage2InitVO);
    }

    @MessageMapping("/team/pin") // 핀 위치 변경 시, 동일 팀원+guess 마친 팀들에게 변경한 핀 위치 broadcast
    public void movePin(PinMoveRequestDTO pinMoveRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        PinMoveVO pinMoveVO = gameService.movePin(gamerId, pinMoveRequestDTO);

        // `/team/{gameId}/{teamId}` & `/guess/{gameId}`를 구독 중인 모든 사용자에게 publish
        // TODO: 전달할 destination 재검토 필요. /team/~ 에서 gameId가 경로에 함께 들어가야 하는가?
        sendingOperations.convertAndSend("/team/" + pinMoveVO.getSenderGameId() + "/" + pinMoveVO.getSenderTeamId(), pinMoveVO);
        sendingOperations.convertAndSend("/guess/" + pinMoveVO.getSenderGameId(), pinMoveVO);
    }

    @MessageMapping("/team/guess")
    public void guessPin(PinGuessRequestDTO pinGuessRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        PinGuessVO pinGuessVO = gameService.guessPin(gamerId, pinGuessRequestDTO);

        // `/team/{gameId}/{teamId}` & `/guess/{gameId}`를 구독 중인 모든 사용자에게 publish
        // TODO: 전달할 destination 재검토 필요. /team/~ 에서 gameId가 경로에 함께 들어가야 하는가?
        // TODO: /guess/{gameId} 구독자에게도 broadcast해야 하는 정보인지 재검토 필요
        sendingOperations.convertAndSend("/team/" + pinGuessVO.getSenderGameId() + "/" + pinGuessVO.getSenderTeamId(), pinGuessVO);
        sendingOperations.convertAndSend("/guess/" + pinGuessVO.getSenderGameId(), pinGuessVO);
    }
}