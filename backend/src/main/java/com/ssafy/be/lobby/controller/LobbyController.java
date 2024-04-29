package com.ssafy.be.lobby.controller;

import com.ssafy.be.lobby.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lobby")
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;

}

