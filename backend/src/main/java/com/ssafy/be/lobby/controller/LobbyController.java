package com.ssafy.be.lobby.controller;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

import com.ssafy.be.common.component.Game;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.component.Team;
import com.ssafy.be.lobby.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.be.lobby.model.dto.RoomDTO;

import com.ssafy.be.common.response.BaseResponse;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("lobby")
@RequiredArgsConstructor
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameManager gameManager;
//    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    /*
    * return : game_id,
    * */
    @MessageMapping("/game")
    public void createRoom(@Payload Game game){
        ConcurrentHashMap<String, Game> games = gameManager.getGames();


        System.out.println(game);
        simpMessagingTemplate.convertAndSend("/game" , game);
    }
}

