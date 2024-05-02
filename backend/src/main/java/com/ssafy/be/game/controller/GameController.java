package com.ssafy.be.game.controller;

import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessageSendingOperations sendingOperations;

    @Autowired
    private GameController(GameService gameService, SimpMessageSendingOperations sendingOperations) {
        this.gameService = gameService;
        this.sendingOperations = sendingOperations;
    }

    /////
    // TODO: 요청 보낸 사용자가 게임 참가자인지 확인 & 한 게임에 대해 중복 요청 검증 처리 필요

    @MessageMapping("/game/start")
    public void startGame(GameStartRequestDTO gameStartRequestDTO) {
        GameStartResponseDTO gameStartResponseDTO = gameService.startGame(gameStartRequestDTO);
        sendingOperations.convertAndSend("/game/start/"+gameStartRequestDTO.getGameId(), gameStartResponseDTO);
    }


    @MessageMapping("/game/init") // /app/game/init 으로 보내면 여기로 옴
    public void initGame(GameInitRequestDTO gameInitRequestDTO) {
        System.out.println(gameInitRequestDTO.toString());
        GameInitResponseDTO gameInitResponseDTO = gameService.findGameInfo(gameInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/init/" + gameInitResponseDTO.getGameId(), gameInitResponseDTO);
    }

    @MessageMapping("/game/round/init")
    public void initStage1(RoundInitRequestDTO roundInitRequestDTO) {
        RoundInitResponseDTO roundInitResponseDTO = gameService.findStage1Info(roundInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/round/init/" + roundInitResponseDTO.getGameId(), roundInitResponseDTO);
    }

    @MessageMapping("/game/round/stage2/init")
    public void initStage2(Stage2InitRequestDTO stage2InitRequestDTO) {
        Stage2InitResponseDTO stage2InitResponseDTO = gameService.findStage2Info(stage2InitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/round/stage2/init/" + stage2InitResponseDTO.getGameId(), stage2InitResponseDTO);
    }


}
