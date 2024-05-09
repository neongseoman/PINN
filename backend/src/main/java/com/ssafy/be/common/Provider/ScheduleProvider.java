package com.ssafy.be.common.Provider;


import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.game.model.vo.GameInitVO;
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
            future.complete(gameId);
        },5, TimeUnit.SECONDS);
        return future;
    }
    // Stage 1으로 진입하는 것.
    public CompletableFuture<Integer> scheduleFuture(int gameId, int delayTime)  throws BaseException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
            future.complete(gameId);
        }, delayTime, TimeUnit.SECONDS);
        return future;
    }


}
