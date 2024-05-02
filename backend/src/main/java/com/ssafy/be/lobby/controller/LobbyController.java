package com.ssafy.be.lobby.controller;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.lobby.model.dto.CreateRoomDTO;
import com.ssafy.be.lobby.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    * 방 생성을 위한 API
    * URL : /lobby/create
    * return : game_id
    * */
    @GetMapping("create")
    public BaseResponse<?> createRoom(@RequestBody CreateRoomDTO createRoomDTO){
        // gamer_id, 즉 방을 생성한 방리더의 '검증된' id 추출하여 game에 삽입
        // TODO : leader_id 수정
        createRoomDTO.setLeader_id(2);
        // 생성된 초기 게임 설정을 DB에 저장
        GameComponent savedGame = lobbyService.createRoom(createRoomDTO);
        // GameManager에 해당 데이터 추가
        gameManager.addGame(savedGame);

        return new BaseResponse<>(savedGame);
    }

    /*
     * 방 생성을 위한 API
     * URL : /lobby/create
     * return :
     * */
    @MessageMapping("/game/{gameId}")
    @SendTo("/app/game/{gameId}")
    public String createRoom(@Payload String msg, @DestinationVariable String gameId){
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();

        System.out.println(msg);
//        simpMessagingTemplate.convertAndSend(des , game);

        return msg.toString();
    }
}

