package com.ssafy.be.lobby.service;

import com.ssafy.be.common.component.GameComponent;
import org.springframework.stereotype.Service;

public interface LobbyService {
    void createRoom(GameComponent game);
}
