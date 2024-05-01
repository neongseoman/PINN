package com.ssafy.be.lobby.controller;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.lobby.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("lobby")
@RequiredArgsConstructor
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameManager gameManager;

    private final SimpMessagingTemplate simpMessagingTemplate;

    /*
    * return : game_id,
    * */
    @MessageMapping("/game")
    public void createRoom(@Payload GameComponent game){
        ConcurrentHashMap<String, GameComponent> games = gameManager.getGames();
        // gamer_id, 즉 방을 생성한 방리더의 '검증된' id 추출하여 game에 삽입

        // 생성된 초기 게임 설정을 DB에 저장
        lobbyService.createRoom(game);

        System.out.println(game);
        simpMessagingTemplate.convertAndSend("/game" , game);
    }
}

