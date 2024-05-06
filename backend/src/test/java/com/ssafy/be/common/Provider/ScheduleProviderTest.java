package com.ssafy.be.common.Provider;

import com.ssafy.be.common.model.dto.ServerEvent;
import com.ssafy.be.common.model.dto.ServerSendEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ScheduleProviderTest {
    int gameId = 1234;
    private static final Logger log = Logger.getLogger(ScheduleProviderTest.class.getName());
//    @Mock
//    private ScheduledExecutorService executorService;
//
//    @InjectMocks
    private static ScheduleProvider scheduleProvider;
    @BeforeAll
    static void init(){
        scheduleProvider = new ScheduleProvider();
    }

    @Test
    void startGame() throws ExecutionException, InterruptedException, TimeoutException {
        LocalDateTime startTime = LocalDateTime.now();

        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
        log.info(String.valueOf(startTime));
        CompletableFuture<Integer> future = scheduleProvider.startGame(gameId)
                .thenCompose((result) -> {
                    log.info("{} : Starting gameId : {} at {}" +result + gameId+ LocalDateTime.now());
//                    sendingOperations.convertAndSend("/game/"+result,new ServerSendEvent(ServerEvent.START));
                    return scheduleProvider.roundStart(result);
                }).thenCompose((result)->{
//                    log.info("{} : round start: {}  at {}" +result + gameId+ LocalDateTime.now());
                    return scheduleProvider.sendHint(result,5);
                });

        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
        int completedGameId = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
        LocalDateTime endTime = LocalDateTime.now();

        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);

        // 결과 확인
        assertEquals(gameId, completedGameId); // 반환된 게임 ID가 일치하는지 확인
//        assertTrue(secondsDifference >= 5); // 최소 5초 지연이 있었는지 확인

        System.out.printf("Expected delay: 5 seconds, Actual delay: %d seconds%n", secondsDifference);

    }

    @Test
    void roundStart() {
    }

    @Test
    void sendHint() {
    }

    @Test
    void roundEnd() {
    }

    @Test
    void nextRound() {
    }
}