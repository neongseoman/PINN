package com.ssafy.be.lobby.controller;

import com.ssafy.be.common.component.Game;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.component.Team;
import com.ssafy.be.lobby.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.be.lobby.model.dto.RoomDTO;

import com.ssafy.be.common.response.BaseResponse;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("lobby")
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameManager gameManager;

    @MessageMapping("/lobby/create")
    public BaseResponse<?> createRoom(@Payload RoomDTO roomDTO){
        ConcurrentHashMap<String, Game> games = gameManager.getGames();
        return new BaseResponse<>("hello ws");
    }
}

