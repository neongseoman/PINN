package com.ssafy.be.game.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.Provider.ScheduleProvider;
import com.ssafy.be.common.model.dto.ServerEvent;
import com.ssafy.be.common.model.dto.ServerSendEvent;
import com.ssafy.be.game.model.dto.*;
import com.ssafy.be.game.model.vo.*;
import com.ssafy.be.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

@RestController
@Log4j2
@RequestMapping("/game")
@RequiredArgsConstructor()
public class GameController {
    private final ScheduleProvider scheduleProvider;
    private final GameService gameService;
    private final SimpMessageSendingOperations sendingOperations;
    private final JwtProvider jwtProvider;

    /////
    // TODO: 한 게임에 대해 중복 요청 검증 처리 필요
    // 단순 game status 변경 + 참가자들에게 시작 소식 broadcast 하여 로딩 화면으로 넘어갈 수 있도록 함

    @MessageMapping("/game/start")
    public void startGame(GameStartRequestDTO gameStartRequestDTO, StompHeaderAccessor accessor) throws ExecutionException, InterruptedException {
//        log.info("Is this async? Start of Method : {}", LocalDateTime.now());
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();
        GameStartVO gameStartVO = gameService.startGame(gamerId, gameStartRequestDTO);
        GameInitVO gameInitVO = gameService.initGame(gamerId, gameStartRequestDTO);

        log.info("{} game started at {}", gameStartRequestDTO.getGameId(), LocalDateTime.now());
        sendingOperations.convertAndSend("/game/" + gameInitVO.getGameId(), new ServerSendEvent(ServerEvent.START));

        CompletableFuture<Integer> startFuture = scheduleProvider.startGame(gameStartRequestDTO.getGameId(), gameInitVO);

        startFuture.thenCompose((result) -> { // startFuture에서 5초 보냈고 Game Init도 보냄.
            log.info("{} game round 1 started at {}", gameStartRequestDTO.getGameId(), LocalDateTime.now());
            RoundInitRequestDTO roundInitRequestDTO =
                    new RoundInitRequestDTO(gameStartVO.getSenderNickname(), gameStartVO.getSenderGameId(),
                            gameStartVO.getSenderTeamId(), gameStartVO.getGameId(), 1);
            RoundInitVO roundInitVO = gameService.findStage1Info(gamerId, roundInitRequestDTO);
            roundInitVO.setCode(ServerEvent.ROUND_START.getCode());
            roundInitVO.setMsg(ServerEvent.ROUND_START.getMsg());
            sendingOperations.convertAndSend("/game/" + gameInitVO.getGameId(), roundInitVO); // 여기까지 게임 준비 및 시작

//            return scheduleProvider.sendHint(result, gameStartRequestDTO.getStage1Time()); // 지금부터 stage 1
            return scheduleProvider.sendHint(result, 5); // 지금부터 stage 1 test time 5초
        }).thenCompose((result) -> {
            log.info("{} game send Hint at {}", gameStartRequestDTO.getGameId(), LocalDateTime.now());
            Stage2InitRequestDTO stage2InitRequestDTO =
                    new Stage2InitRequestDTO(gameStartVO.getSenderNickname(), gameStartVO.getSenderGameId(),
                            gameStartVO.getSenderTeamId(), gameStartVO.getGameId(), 1); // 현재 라운드 번호
            Stage2InitVO stage2InitVO = gameService.findStage2Info(gamerId, stage2InitRequestDTO);
            stage2InitVO.setCode(ServerEvent.HINT.getCode());
            stage2InitVO.setMsg(ServerEvent.HINT.getMsg());
            sendingOperations.convertAndSend("/game/" + gameInitVO.getGameId(), stage2InitVO);

//            return scheduleProvider.roundEnd(result, gameStartRequestDTO.getStage2Time()); // 지금부터 stage 2
            return scheduleProvider.roundEnd(result, 5); // 지금부터 stage 2 test time 5초
        }).thenCompose((result) -> {
            log.info("{} game round 1 is end at {}", gameStartRequestDTO.getGameId(), LocalDateTime.now()); // 점수 정산

            sendingOperations.convertAndSend("/game/" + gameInitVO.getGameId(), new ServerSendEvent(ServerEvent.ROUND_SCORE));
//            return scheduleProvider.roundEnd(result, gameStartRequestDTO.getScorePageTime()); // 지금부터 score page
            return scheduleProvider.roundEnd(result, 5); // 지금부터 score page
        }).thenCompose((a) -> { // 이건 게임이 끝나
            log.info(" {}  Game is End at {}", gameInitVO.getGameId(), LocalDateTime.now());
            ServerSendEvent serverMsg = new ServerSendEvent(ServerEvent.GO_TO_ROOM);
            sendingOperations.convertAndSend("/game/" + gameInitVO.getGameId(), serverMsg);

            return scheduleProvider.goToRoom(gameInitVO.getGameId(), 5); // 방으로 돌아가라
        }).exceptionally(ex -> {
            log.error("Error occurred in the CompletableFuture chain: ", ex);
            return null;
        });

//        log.info("Is this async? End of Method : {}", LocalDateTime.now());

    }

    @MessageMapping("/game/round/init") // 라운드 시작(문제의 lat, lng + stage1 hint broadcast)
    public void initStage1(RoundInitRequestDTO roundInitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        RoundInitVO roundInitVO = gameService.findStage1Info(gamerId, roundInitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + roundInitVO.getGameId(), roundInitVO);
    }

    @MessageMapping("/game/round/stage2/init") // stage2 hint broadcast
    public void initStage2(Stage2InitRequestDTO stage2InitRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        Stage2InitVO stage2InitVO = gameService.findStage2Info(gamerId, stage2InitRequestDTO);

        // /game/{gameId} 를 구독 중인 모든 사용자에게 publish
        sendingOperations.convertAndSend("/game/" + stage2InitVO.getGameId(), stage2InitVO);
    }

    @MessageMapping("/team/pin") // 핀 위치 변경 시, 동일 팀원+guess 마친 팀들에게 변경한 핀 위치 broadcast
    public void movePin(PinMoveRequestDTO pinMoveRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        PinMoveVO pinMoveVO = gameService.movePin(gamerId, pinMoveRequestDTO);

        // `/team/{gameId}/{teamId}` & `/guess/{gameId}`를 구독 중인 모든 사용자에게 publish
        // TODO: 전달할 destination 재검토 필요. /team/~ 에서 gameId가 경로에 함께 들어가야 하는가?
        sendingOperations.convertAndSend("/team/" + pinMoveVO.getSenderGameId() + "/" + pinMoveVO.getSenderTeamId(), pinMoveVO);
        sendingOperations.convertAndSend("/guess/" + pinMoveVO.getSenderGameId(), pinMoveVO);
    }

    @MessageMapping("/team/guess")
    public void guessPin(PinGuessRequestDTO pinGuessRequestDTO, StompHeaderAccessor accessor) {
        int gamerId = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor).getGamerId();

        PinGuessVO pinGuessVO = gameService.guessPin(gamerId, pinGuessRequestDTO);

        // `/team/{gameId}/{teamId}` & `/guess/{gameId}`를 구독 중인 모든 사용자에게 publish
        // TODO: 전달할 destination 재검토 필요. /team/~ 에서 gameId가 경로에 함께 들어가야 하는가?
        // TODO: /guess/{gameId} 구독자에게도 broadcast해야 하는 정보인지 재검토 필요
        sendingOperations.convertAndSend("/team/" + pinGuessVO.getSenderGameId() + "/" + pinGuessVO.getSenderTeamId(), pinGuessVO);
        sendingOperations.convertAndSend("/guess/" + pinGuessVO.getSenderGameId(), pinGuessVO);
    }
}