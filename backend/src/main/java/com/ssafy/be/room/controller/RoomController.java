package com.ssafy.be.room.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.model.dto.ChatDTO;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.lobby.model.ReadyGame;
import com.ssafy.be.room.model.dto.MoveTeamDTO;
import com.ssafy.be.room.model.vo.MoveTeamVO;
import com.ssafy.be.room.model.vo.TeamStatusVO;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("{gameId}")
    public BaseResponse<?> getGame(@PathVariable Integer gameId){
        ReadyGame game = gameManager.getGame(gameId);

        return new BaseResponse<>(game);
    }


    // ---------------------------------- SOCKET ---------------------------------------------


    /*
     * 팀 옮기기 Socket 메서드
     * subscribe : /game/{gameId}
     * publish : /app/game/moveTeam/{gameId}
     * send to : /game/{gameId}
     * */
    @MessageMapping("/game/moveTeam/{gameId}")
    @SendTo("/game/{gameId}")
    public MoveTeamVO moveTeam(@Payload MoveTeamDTO moveTeamDTO, @DestinationVariable Integer gameId, StompHeaderAccessor accessor){
        GamerPrincipalVO gamerPrincipalVO = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor);

        // 일단 팀 나가기
        moveTeamDTO.setSenderGameId(gameId);
        moveTeamDTO.setSenderTeamId(moveTeamDTO.getOldTeamId());
        gameManager.exitRoom(moveTeamDTO, gamerPrincipalVO, true);
//        gameManager.exitTeam(moveTeamDTO, gamerPrincipalVO);
        // 새로운 팀에 할당
        MoveTeamVO moveTeamVO = gameManager.enterSpecificTeam(moveTeamDTO, gamerPrincipalVO);

        return moveTeamVO;
    }

    /*
     * 대기방 내 채팅을 위한 Socket 메서드
     * subscribe : /game/{gameId}
     * publish : /app/game/chat/{gameId}
     * send to : /game/{gameId}
     * */
    @MessageMapping("/game/chat/{gameId}")
    @SendTo("/game/{gameId}")
    public ChatDTO chatRoom(ChatDTO chatDTO, @DestinationVariable String gameId){
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();
        // nickname 검증

//        System.out.println(chatDTO);

        // 방 채팅, 팀 채팅
        // code & msg 삽입
        chatDTO.setCodeAndMsg(1001, "방 채팅이 성공적으로 보내졌습니다.");

        return chatDTO;
    }

    /*
     * 팀의 상태를 변경하기 위한 Socket 메서드 ( ready <-> !ready )
     * subscribe : /game/{gameId}
     * publish : /app/game/teamStatus/{gameId}
     * send to : /game/{gameId}
     * */
    @MessageMapping("/game/teamStatus/{gameId}")
    @SendTo("/game/{gameId}")
    public TeamStatusVO changeTeamStatus(SocketDTO socketDTO, @DestinationVariable int gameId, StompHeaderAccessor accessor){
        GamerPrincipalVO gamerPrincipalVO = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor);
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();

        socketDTO.setSenderGameId(gameId);

        TeamStatusVO teamStatusVO =  gameManager.changeTeamStatus(socketDTO, gamerPrincipalVO);
        return teamStatusVO;
    }

}
