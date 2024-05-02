package com.ssafy.be.game.controller;

import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private final GameService gameService;
    private final SimpMessageSendingOperations sendingOperations;

    @Autowired
    private GameController(GameService gameService, SimpMessageSendingOperations sendingOperations) {
        this.gameService = gameService;
        this.sendingOperations = sendingOperations;
    }

    /////
    // TODO: 요청 보낸 사용자가 방장인지 확인 & 중복 요청 검증 처리 필요

    @MessageMapping("/game/init")
    public void initGame(GameInitRequestDTO gameInitRequestDTO) {
        GameInitResponseDTO gameInitResponseDTO = gameService.findGameInfo(gameInitRequestDTO);
        sendingOperations.convertAndSend("/game/"+gameInitResponseDTO.getGameId(), gameInitResponseDTO);
    }

    @MessageMapping("/game/stage1/{gameId}")
    public void initStage1(RoundInitRequestDTO roundInitRequestDTO) {
        RoundInitResponseDTO roundInitResponseDTO = gameService.findStage1Info(roundInitRequestDTO);
        sendingOperations.convertAndSend("/game/"+roundInitResponseDTO.getGameId(), roundInitResponseDTO);
    }

    @MessageMapping("/game/stage2/{gameId}")
    public void initStage2(Stage2InitRequestDTO stage2InitRequestDTO) {
        Stage2InitResponseDTO stage2InitResponseDTO = gameService.findStage2Info(stage2InitRequestDTO);
        sendingOperations.convertAndSend("/game/"+stage2InitResponseDTO.getGameId(), stage2InitResponseDTO);
    }


}
