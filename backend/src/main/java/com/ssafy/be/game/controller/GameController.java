package com.ssafy.be.game.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
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

@RestController
@Log4j2
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessageSendingOperations sendingOperations;
    private final JwtProvider jwtProvider;

    @Autowired
    private GameController(GameService gameService, SimpMessageSendingOperations sendingOperations, JwtProvider jwtProvider) {
        this.gameService = gameService;
        this.sendingOperations = sendingOperations;
        this.jwtProvider = jwtProvider;
    }

    /////
    // TODO: 한 게임에 대해 중복 요청 검증 처리 필요


    @MessageMapping("/game/start") // 단순 game status 변경 + 참가자들에게 시작 소식 broadcast 하여 로딩 화면으로 넘어갈 수 있도록 함
    public void startGame(GameStartRequestDTO gameStartRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        GameStartResponseDTO gameStartResponseDTO = gameService.startGame(gamerId, gameStartRequestDTO);
        sendingOperations.convertAndSend("/game/" + gameStartResponseDTO.getGameId(), gameStartResponseDTO);
    }

    @MessageMapping("/game/init") // 게임 문제 배정 + 게임 초기 정보값 broadcast
    public void initGame(GameInitRequestDTO gameInitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        GameInitResponseDTO gameInitResponseDTO = gameService.initGame(gamerId, gameInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + gameInitResponseDTO.getGameId(), gameInitResponseDTO);
    }

    @MessageMapping("/game/round/init") // 라운드 시작(문제의 lat, lng + stage1 hint broadcast)
    public void initStage1(RoundInitRequestDTO roundInitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        RoundInitResponseDTO roundInitResponseDTO = gameService.findStage1Info(gamerId, roundInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + roundInitResponseDTO.getGameId(), roundInitResponseDTO);
    }

    @MessageMapping("/game/round/stage2/init") // stage2 hint broadcast
    public void initStage2(Stage2InitRequestDTO stage2InitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        Stage2InitResponseDTO stage2InitResponseDTO = gameService.findStage2Info(gamerId, stage2InitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + stage2InitResponseDTO.getGameId(), stage2InitResponseDTO);
    }

    @MessageMapping("/team/pin") // 핀 위치 변경 시, 동일 팀원+guess 마친 팀들에게 변경한 핀 위치 broadcast
    public void movePin(PinMoveRequestDTO pinMoveRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        PinMoveResponseDTO pinMoveResponseDTO = gameService.movePin(gamerId, pinMoveRequestDTO);

        // `/team/{gameId}/{teamId}` & `/guess/{gameId}`를 구독 중인 모든 사용자에게 publish
        // TODO: 전달할 destination 재검토 필요. /team/~ 에서 gameId가 경로에 함께 들어가야 하는가?
        sendingOperations.convertAndSend("/team/" + pinMoveResponseDTO.getSenderGameId() + "/" + pinMoveResponseDTO.getSenderTeamId(), pinMoveResponseDTO);
        sendingOperations.convertAndSend("/guess/" + pinMoveResponseDTO.getSenderGameId(), pinMoveResponseDTO);
    }

}