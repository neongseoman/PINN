package com.ssafy.be.common.Provider;


import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.model.dto.ServerEvent;
import com.ssafy.be.common.model.dto.ServerSendEvent;
import com.ssafy.be.game.model.dto.GameStartRequestDTO;
import com.ssafy.be.game.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.*;

@Component
@Log4j2
@RequiredArgsConstructor
public class ScheduleProvider {
    private final GameService gameService;
    private final SimpMessageSendingOperations sendingOperations;
    private final JwtProvider jwtProvider;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();


    // 5초 지났고 게임 시작합시다.
    public CompletableFuture<Integer> startGame(int gameId) throws BaseException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
            log.info("{} game start after 5 sec : {}", gameId, LocalDateTime.now());
            sendingOperations.convertAndSend("/game/sse/" + gameId,
                    new ServerSendEvent(ServerEvent.START)); // send Hint and Stage 1 End # 1203
            future.complete(gameId);
        }, 5, TimeUnit.SECONDS);
        return future;
    }

    // Stage 1으로 진입하는 것.
    public CompletableFuture<Integer> scheduleFuture(int gameId, int delayTime) throws BaseException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
            future.complete(gameId);
        }, delayTime, TimeUnit.SECONDS);
        return future;
    }

    public CompletableFuture<Boolean> roundScheduler(int gameId, GameStartRequestDTO gameStartRequestDTO,int currentRound) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        currentRound += 1;
        log.info("{} Round {} Start: {}", gameStartRequestDTO.getGameId(), currentRound,LocalDateTime.now());
        sendingOperations.convertAndSend("/game/sse/" + gameId,
                new ServerSendEvent(ServerEvent.ROUND_START)); // Game Start # 1202
        int finalCurrentRound = currentRound;
        scheduleFuture(gameId, gameStartRequestDTO.getStage1Time())
                .thenCompose(r -> { // Round Start stage 1
                    log.info("{} game stage 1 End : {}", gameStartRequestDTO.getGameId(), LocalDateTime.now());
                    sendingOperations.convertAndSend("/game/sse/" + gameId,
                            new ServerSendEvent(ServerEvent.STAGE_1_END)); // send Hint and Stage 1 End # 1203
                    return scheduleFuture(gameId, gameStartRequestDTO.getStage1Time());  // Stage 2 기다리기
                }).thenCompose(r -> { // Stage 2
                    log.info("{} game stage 2 End : {}", gameStartRequestDTO.getGameId(), LocalDateTime.now());
                    sendingOperations.convertAndSend("/game/sse/" + gameId,
                            new ServerSendEvent(ServerEvent.STAGE_2_END)); // Stage 2 End go To Score # 1204
                    return scheduleFuture(gameId, gameStartRequestDTO.getStage2Time());
                }).thenCompose(r -> { // Round Score
                    log.info("{} game Score page  : {}", gameStartRequestDTO.getGameId(), LocalDateTime.now());
                    sendingOperations.convertAndSend("/game/sse/" + gameId,
                            new ServerSendEvent(ServerEvent.SCORE_PAGE)); // Round End stage 1, 2 score # 1205
                    return scheduleFuture(gameId, gameStartRequestDTO.getScorePageTime());
                }).thenRun(() -> {
                    log.info("{} game {} Round End  : {}", gameStartRequestDTO.getGameId(), finalCurrentRound, LocalDateTime.now());
                    sendingOperations.convertAndSend("/game/sse/" + gameId,
                            new ServerSendEvent(ServerEvent.SCORE_PAGE)); // Round End stage 1, 2 score # 1205
                    future.complete(true);
                });
        return future;
    }

    public CompletableFuture<Boolean> gameFinishScheduler(int gameId, GameStartRequestDTO gameStartRequestDTO) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        log.info("{} All Round is End Go to Game Score: {}", gameStartRequestDTO.getGameId(), LocalDateTime.now());
        sendingOperations.convertAndSend("/game/sse/" + gameId,
                new ServerSendEvent(ServerEvent.GAME_SCORE)); // Round End stage 1, 2 score # 1205
        scheduleFuture(gameId, 120)
                .thenCompose(r -> {
                    log.info("{} Game is End Go to Room: {}", gameStartRequestDTO.getGameId(), LocalDateTime.now());
                    sendingOperations.convertAndSend("/game/sse/" + gameId,
                            new ServerSendEvent(ServerEvent.GO_TO_ROOM)); // Round End stage 1, 2 score # 1205
                    return scheduleFuture(gameId, 180);
                })
                .thenRun(() ->
                        future.complete(true)
                );
        return future;
    }


}
