package com.ssafy.be.game.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.Provider.ScheduleProvider;
import com.ssafy.be.common.model.dto.ServerEvent;
import com.ssafy.be.common.model.dto.ServerSendEvent;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.service.GameService;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    // TODO: 요청 보낸 사용자가 해당 게임의 방장인지 확인 & 한 게임에 대해 중복 요청 검증 처리 필요

    @MessageMapping("/game/start") // 단순 game status 변경 + 참가자들에게 시작 소식 broadcast 하여 로딩 화면으로 넘어갈 수 있도록 함
    public void startGame(GameStartRequestDTO gameStartRequestDTO, StompHeaderAccessor accessor) throws ExecutionException, InterruptedException {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();
        GameStartResponseDTO gameStartResponseDTO = gameService.startGame(gamerId, gameStartRequestDTO);
        CompletableFuture<Integer> startGameFuture = scheduleProvider.startGame(gamerId)
                .toCompletableFuture();
//        CompletableFuture<Integer> round1StartFuture = startGameFuture.thenAccept(startGameId -> {
//            sendingOperations.convertAndSend("/game/"+startGameId,new ServerSendEvent(ServerEvent.START));
//            scheduleProvider.roundStart(startGameId);
//        });
        sendingOperations.convertAndSend("/game/" + gameStartResponseDTO.getGameId(), gameStartResponseDTO);
    }

    @MessageMapping("/game/init")
    public void initGame(GameInitRequestDTO gameInitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();
        GameInitResponseDTO gameInitResponseDTO = gameService.findGameInfo(gamerId, gameInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + gameInitResponseDTO.getGameId(), gameInitResponseDTO);
    }

    @MessageMapping("/game/round/init")
    public void initStage1(RoundInitRequestDTO roundInitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        RoundInitResponseDTO roundInitResponseDTO = gameService.findStage1Info(gamerId, roundInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + roundInitResponseDTO.getGameId(), roundInitResponseDTO);
    }

    @MessageMapping("/game/round/stage2/init")
    public void initStage2(Stage2InitRequestDTO stage2InitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        Stage2InitResponseDTO stage2InitResponseDTO = gameService.findStage2Info(gamerId, stage2InitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + stage2InitResponseDTO.getGameId(), stage2InitResponseDTO);
    }


}