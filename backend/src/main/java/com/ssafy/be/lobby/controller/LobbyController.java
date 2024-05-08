package com.ssafy.be.lobby.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.component.GameComponent;
import com.ssafy.be.common.component.GameManager;
import com.ssafy.be.common.component.TeamGamerComponent;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import com.ssafy.be.lobby.model.dto.CreateRoomDTO;
import com.ssafy.be.lobby.model.vo.SearchVO;
import com.ssafy.be.lobby.service.LobbyService;
import jakarta.servlet.ServletRequest;
import java.util.HashMap;
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

    private final JwtProvider jwtProvider;


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
        log.info("createRoomDTO : " + createRoomDTO);
        // 생성된 초기 게임 설정을 DB에 저장
        GameComponent savedGame = lobbyService.createRoom(createRoomDTO);
        // 게임 내 팀 객체를 생성
        lobbyService.createTeams(savedGame);
        // GameManager에 게임 추가
        gameManager.addGame(savedGame);

        return new BaseResponse<>(savedGame);
    }

    /*
     * 방 입장이 가능한지 확인하는 API
     * method : GET
     * URL : /lobby/{gameId}
     * return :
     * */
    @PostMapping("{gameId}")
    public BaseResponse<?> enterRoom(@PathVariable Integer gameId, @RequestBody(required = false) HashMap<String,String> body, ServletRequest req){
        // gamer_id, 즉 방을 생성한 방리더의 '검증된' id 추출하여 game에 삽입
        GamerPrincipalVO gamerPrincipalVO = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        log.info(gamerPrincipalVO.getGamerId());

        // 존재하는 게임인지, 비밀번호 맞는지,
        log.info("client Password : " + body.get("password"));
        return gameManager.isGame(gameId, body.get("password"));

    }


    /*
     * 현재 실행 중인 게임을 확인하기 위한 메서드
     * method : GET
     * URI : lobby/checkGameManager
    // TODO : 배포시 리턴 객체 지우기!!!
     */
    @GetMapping("checkGameManager")
    public BaseResponse<?> checkGameManager(){
        System.out.println(gameManager.getGames());
        return new BaseResponse<>(BaseResponseStatus.ENTER_SUCCESS, gameManager.getGames());
    }

     /*
     Ready 상태의 입장 가능한 방을 반환하는 메서드
     method : GET
     URI : lobby/checkGameManager
     */
    @GetMapping("search")
    public BaseResponse<?> searchRoom(){

        SearchVO searchVO = lobbyService.searchRoom();

        return new BaseResponse<>(searchVO);
    }


    // ---------------------------------- SOCKET ---------------------------------------------

    /*
     * 룸 입장을 위한 Socket 메서드 - [GET] lobby/{gameId} 로 검증 이후 요청해야 함
     * subscribe : /game/{gameId}
     * publish : /app/game/enter/{gameId}
     * send to : /game/{gameId}
     * */
    @MessageMapping("/game/enter/{gameId}")
    @SendTo("/game/{gameId}")
    public SocketDTO enterRoom(@Payload SocketDTO socketDTO, @DestinationVariable Integer gameId, StompHeaderAccessor accessor){
        GamerPrincipalVO gamerPrincipalVO = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor);
        log.info(gamerPrincipalVO.getGamerId());
        ConcurrentHashMap<Integer, GameComponent> games = gameManager.getGames();

        // 오름차순으로 비어있는 팀에 할당
        TeamGamerComponent teamGamerComponent = gameManager.enterTeam(games.get(gameId), gamerPrincipalVO.getGamerId());

        // code & msg 삽입
        socketDTO.setCodeAndMsg(1002, "gameId : " + gameId + " 방에 " + teamGamerComponent.getTeamId() + "팀으로 " + gamerPrincipalVO.getNickname() + "님이 들어왔습니다.");
        // 팀 할당
        socketDTO.setSenderTeamId(teamGamerComponent.getTeamId());
        log.info(socketDTO);
        return socketDTO;
    }

    /*
     * 룸 퇴장 Socket 메서드
     * subscribe : /game/{gameId}
     * publish : /game/exit/{gameId}
     * send to : /app/game/{gameId}
     * */
    @MessageMapping("/game/exit/{gameId}")
    @SendTo("/game/{gameId}")
    public SocketDTO exitRoom(@Payload SocketDTO socketDTO, @DestinationVariable Integer gameId, StompHeaderAccessor accessor){
        GamerPrincipalVO gamerPrincipalVO = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor);

        gameManager.exitRoom(socketDTO, gamerPrincipalVO.getGamerId());
        // code & msg 설정
        socketDTO.setCodeAndMsg(1003, gamerPrincipalVO.getNickname() + "님이 " + socketDTO.getSenderGameId() + "방에서 나갔습니다.");

        return socketDTO;
    }




}

