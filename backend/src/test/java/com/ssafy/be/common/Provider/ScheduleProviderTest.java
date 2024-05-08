package com.ssafy.be.common.Provider;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("게임 각 부분 진행 시간 테스트")
@ExtendWith(MockitoExtension.class)
class ScheduleProviderTest {
    int gameId = 1234;
    int testSecond = 5;
    private static final Logger log = Logger.getLogger(ScheduleProviderTest.class.getName());

    @Mock
    private SimpMessageSendingOperations sendingOperations;

    @InjectMocks
    private ScheduleProvider scheduleProvider;

//    @DisplayName("5초 후 게임 시작")
//    @Test
//    void _test_startGame() throws ExecutionException, InterruptedException, TimeoutException {
//        LocalDateTime startTime = LocalDateTime.now();
//
//        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
//        log.info(String.valueOf(startTime));
//        CompletableFuture<Integer> future = scheduleProvider.startGame(gameId, gameStartRequestDTO, gameStartVO);
//
//        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
//        int completedGameId = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
//        LocalDateTime endTime = LocalDateTime.now();
//
//        // 시작과 완료 시각의 차이를 구합니다.
//        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);
//
//        // 결과 확인
//        assertEquals(gameId, completedGameId); // 반환된 게임 ID가 일치하는지 확인
//        assertTrue(secondsDifference >= 5); // 최소 5초 지연이 있었는지 확인
//
//        System.out.printf("Expected delay: " +5 + " seconds, Actual delay: %d seconds%n", secondsDifference);
//
//    }

    @DisplayName("라운드 시작")
    @Test
    void roundStart()  throws ExecutionException, InterruptedException, TimeoutException  {
        LocalDateTime startTime = LocalDateTime.now();

        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
        log.info(String.valueOf(startTime));
        CompletableFuture<Integer> future = scheduleProvider.roundStart(gameId,testSecond);

        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
        int completedGameId = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
        LocalDateTime endTime = LocalDateTime.now();

        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);

        // 결과 확인
        assertEquals(gameId, completedGameId); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= testSecond); // 최소 5초 지연이 있었는지 확인

        System.out.printf("Expected delay: " +testSecond + " seconds, Actual delay: %d seconds%n", secondsDifference);

    }

    @DisplayName("Hint 보내고 stage2시작")
    @Test
    void sendHint()  throws ExecutionException, InterruptedException, TimeoutException  {
        LocalDateTime startTime = LocalDateTime.now();

        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
        log.info(String.valueOf(startTime));
        CompletableFuture<Integer> future = scheduleProvider.roundStart(gameId,testSecond);

        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
        int completedGameId = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
        LocalDateTime endTime = LocalDateTime.now();

        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);

        // 결과 확인
        assertEquals(gameId, completedGameId); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= testSecond); // 최소 testSecond초 지연이 있었는지 확인

        System.out.printf("Expected delay: " +testSecond + " seconds, Actual delay: %d seconds%n", secondsDifference);

    }
    @DisplayName("Round 종료")
    @Test
    void roundEnd()  throws ExecutionException, InterruptedException, TimeoutException  {
        LocalDateTime startTime = LocalDateTime.now();

        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
        log.info(String.valueOf(startTime));
        CompletableFuture<Integer> future = scheduleProvider.roundEnd(gameId,testSecond);

        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
        int completedGameId = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
        LocalDateTime endTime = LocalDateTime.now();

        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);

        // 결과 확인
        assertEquals(gameId, completedGameId); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= testSecond); // 최소 testSecond초 지연이 있었는지 확인

        System.out.printf("Expected delay: " +testSecond + " seconds, Actual delay: %d seconds%n", secondsDifference);

    }

    @DisplayName("점수 페이지")
    @Test
    void nextRound()  throws ExecutionException, InterruptedException, TimeoutException  {
        LocalDateTime startTime = LocalDateTime.now();

        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
        log.info(String.valueOf(startTime));
        CompletableFuture<Integer> future = scheduleProvider.nextRound(gameId,testSecond);

        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
        int completedGameId = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
        LocalDateTime endTime = LocalDateTime.now();

        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);

        // 결과 확인
        assertEquals(gameId, completedGameId); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= testSecond); // 최소 testSecond초 지연이 있었는지 확인

        System.out.printf("Expected delay: " +testSecond + " seconds, Actual delay: %d seconds%n", secondsDifference);

    }

    @DisplayName("라운드 1개가 소비하는 시간 stage1+stage2+점수")
    @Test
    void _test_roundGame() throws ExecutionException, InterruptedException, TimeoutException {
        LocalDateTime startTime = LocalDateTime.now();

        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
        log.info(String.valueOf(startTime));
        CompletableFuture<Integer> future = scheduleProvider.roundStart(gameId,testSecond)
                .thenCompose((result) -> {
                    log.info("{} : Starting gameId : {} at {}" +result + gameId+ LocalDateTime.now());
//                    sendingOperations.convertAndSend("/game/"+result,new ServerSendEvent(ServerEvent.START));
                    return scheduleProvider.sendHint(result,testSecond);
                }).thenCompose((result)->{
//                    log.info("{} : round start: {}  at {}" +result + gameId+ LocalDateTime.now());
                    return scheduleProvider.roundEnd(result,testSecond);
                });

        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
        int completedGameId = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
        LocalDateTime endTime = LocalDateTime.now();

        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);

        // 결과 확인
        assertEquals(gameId, completedGameId); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= testSecond* 3L ); // 최소 testSecond*3초 지연이 있었는지 확인

        System.out.printf("Expected delay: " + (testSecond*3) + " seconds, Actual delay: %d seconds%n", secondsDifference);

    }
}