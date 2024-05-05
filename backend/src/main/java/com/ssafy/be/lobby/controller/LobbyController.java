package com.ssafy.be.lobby.controller;

import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.model.domain.Game;
import com.ssafy.be.common.model.dto.ChatDTO;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.lobby.model.dto.CreateRoomDTO;
import com.ssafy.be.lobby.service.LobbyService;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("lobby")
@RequiredArgsConstructor
@Log4j2
public class LobbyController {

    @Autowired
    private LobbyService lobbyService;

    @Autowired
    private GameManager gameManager;

    private final SimpMessagingTemplate simpMessagingTemplate;

    /*
    * 방 생성을 위한 API
    * method : POST
    * URL : /lobby/create
    * return : GameComponent
    * */
    @PostMapping("create")
    public BaseResponse<?> createRoom(@RequestBody CreateRoomDTO createRoomDTO, ServletRequest req){
        // gamer_id, 즉 방을 생성한 방리더의 '검증된' id 추출하여 game에 삽입
        // TODO : leader_id 수정
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        createRoomDTO.setLeader_id(gamerPrincipalVO.getGamerId());
        log.info(gamerPrincipalVO);
        // 생성된 초기 게임 설정을 DB에 저장
        GameComponent savedGame = lobbyService.createRoom(createRoomDTO);
        // 게임 내 팀을 생성
        lobbyService.createTeams(savedGame);
        // GameManager에 게임 추가
        gameManager.addGame(savedGame);

        return new BaseResponse<>(savedGame);
    }

    /*
     * 방 입장을 위한 API
     * method : GET
     * URL : /lobby/{gameId}
     * return :
     * */
    @PostMapping("{gameId}")
    public BaseResponse<?> enterRoom(@PathVariable Integer gameId, ServletRequest req){
        // gamer_id, 즉 방을 생성한 방리더의 '검증된' id 추출하여 game에 삽입
        // TODO : leader_id 수정
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
//        createRoomDTO.setLeader_id(gamerPrincipalVO.getGamerId());
        log.info(gamerPrincipalVO);

        if(gameManager.isGame(gameId)){
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }
        return new BaseResponse<>(BaseResponseStatus.NOT_EXIST_GAME);

    }


    /*
     * 현재 실행 중인 게임을 확인하기 위한 메서드
     * method : GET
     * URI : lobby/checkGameManager
     * */
    // TODO : 배포시 리턴 객체 지우기!!!
    @GetMapping("checkGameManager")
    public BaseResponse<?> checkGameManager(){
        System.out.println(gameManager.getGames());
        return new BaseResponse<>(BaseResponseStatus.ENTER_SUCCESS, gameManager.getGames());
    }


    // ---------------------------------- SOCKET ---------------------------------------------

    /*
     * 룸 입장을 위한 Socket 메서드
     * subscribe : /game/{gameId}
     * send to : /app/game/{gameId}
     * */
    @MessageMapping("/game/enter/{gameId}")
    @SendTo("/game/{gameId}")
    public SocketDTO enterRoom(@Payload SocketDTO socketDTO, @DestinationVariable String gameId){
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();

        // 오름차순으로 비어있는 팀에 할당


        System.out.println(socketDTO);
        return socketDTO;
    }

    /*
     * 대기방 내 채팅을 위한 Socket 메서드
     * subscribe : /game/{gameId}
     * send to : /app/game/{gameId}
     * */
    @MessageMapping("/game/chat/{gameId}")
    @SendTo("/game/{gameId}")
    public ChatDTO createRoom(ChatDTO chatDTO, @DestinationVariable String gameId){
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        // nickname 검증

        System.out.println(chatDTO);

        // 방 채팅, 팀 채팅
        // code & msg 삽입
        chatDTO.setCodeAndMsg(1001, "방 채팅이 성공적으로 보내졌습니다.");

        return chatDTO;
    }



}

