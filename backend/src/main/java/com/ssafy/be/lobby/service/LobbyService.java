package com.ssafy.be.lobby.service;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.lobby.model.dto.CreateRoomDTO;
import com.ssafy.be.lobby.model.vo.SearchVO;
import java.util.List;

public interface LobbyService {
    GameComponent createRoom(CreateRoomDTO createRoomDTO);

    void createTeams(GameComponent savedGame);

    List<SearchVO> searchRoom();
}
