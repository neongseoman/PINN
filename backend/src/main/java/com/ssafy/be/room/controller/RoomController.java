package com.ssafy.be.room.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.model.dto.ChatDTO;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.room.model.dto.MoveTeamDTO;
import com.ssafy.be.room.model.vo.MoveTeamVO;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("room")
@RequiredArgsConstructor
@Log4j2
public class RoomController {

    @Autowired
    private GameManager gameManager;

    private final JwtProvider jwtProvider;




    // ---------------------------------- SOCKET ---------------------------------------------


    /*
     * 팀 옮기기 Socket 메서드
     * subscribe : /game/{gameId}
     * publish : /game/moveTeam/{gameId}
     * send to : /app/game/{gameId}
     * */
    @MessageMapping("/game/moveTeam/{gameId}")
    @SendTo("/game/{gameId}")
    public MoveTeamVO moveTeam(@Payload MoveTeamDTO moveTeamDTO, @DestinationVariable Integer gameId, StompHeaderAccessor accessor){
        GamerPrincipalVO gamerPrincipalVO = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor);

        MoveTeamVO moveTeamVO = gameManager.moveTeam(moveTeamDTO, gamerPrincipalVO.getGamerId());

        return moveTeamVO;
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
