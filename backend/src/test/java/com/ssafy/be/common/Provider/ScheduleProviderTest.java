package com.ssafy.be.common.Provider;

import com.ssafy.be.common.model.dto.ServerEvent;
import com.ssafy.be.game.model.dto.GameStartRequestDTO;
import com.ssafy.be.game.model.dto.RoundFinishRequestDTO;
import com.ssafy.be.game.service.GameServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("게임 각 부분 진행 시간 테스트")
@ExtendWith(MockitoExtension.class)
@Nested
class ScheduleProviderTest {
    int gameId = 1234;
    int testSecond = 5;
    private static final Logger log = Logger.getLogger(ScheduleProviderTest.class.getName());

    @Mock
    private SimpMessageSendingOperations sendingOperations;
    @Mock
    private GameServiceImpl gameService;

    @InjectMocks
    private ScheduleProvider scheduleProvider;

    @DisplayName("5초 후 게임 시작")
    @Test
    void _test_startGame() throws ExecutionException, InterruptedException, TimeoutException {
        LocalDateTime startTime = LocalDateTime.now();

        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
        log.info(String.valueOf(startTime));
        CompletableFuture<Integer> future = scheduleProvider.startGame(gameId, 0);

        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
        int currentRound = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
        LocalDateTime endTime = LocalDateTime.now();

        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);

        // 결과 확인
        assertEquals(0, currentRound);
//        assertEquals(testSecond, ); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= 5); // 최소 5초 지연이 있었는지 확인

        System.out.printf("Expected delay: " + 5 + " seconds, Actual delay: %d seconds%n", secondsDifference);
    }

    @DisplayName("점수 페이지")
    @Test
    void nextRound() throws ExecutionException, InterruptedException, TimeoutException {
        LocalDateTime startTime = LocalDateTime.now();

        // startGame 메서드를 호출하고 CompletableFuture를 받습니다.
        log.info(String.valueOf(startTime));
        CompletableFuture<Integer> future = scheduleProvider.scheduleFuture(gameId, testSecond);

        // CompletableFuture의 결과를 기다리고 완료 시각을 기록합니다.
        int completedGameId = future.get(20, TimeUnit.SECONDS); // 최대 10초까지 기다립니다.
        LocalDateTime endTime = LocalDateTime.now();

        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);

        // 결과 확인
        assertEquals(gameId, completedGameId); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= testSecond); // 최소 testSecond초 지연이 있었는지 확인

        System.out.printf("Expected delay: " + testSecond + " seconds, Actual delay: %d seconds%n", secondsDifference);
    }

    @DisplayName("라운드 1개가 소비하는 시간 stage1+stage2+점수")
    @Test
    void _test_roundGame() throws ExecutionException, InterruptedException, TimeoutException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        GameStartRequestDTO requestDTO =
                new GameStartRequestDTO("testUser", 1,
                        1, 1, 1, 2, 3, 4);

        //when
        int result = scheduleProvider.roundScheduler(1, requestDTO, 1).get();
//        log.info(String.valueOf(LocalDateTime.now()));
        LocalDateTime endTime = LocalDateTime.now();
        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);
//        log.info(String.valueOf(secondsDifference));
        int expectDelay = 2 + 3 + 4;
        // then
        assertEquals(1, result); // 3라운드까지 잘 카운트가 되엇나?
        assertEquals(requestDTO.getStage1Time() + requestDTO.getStage2Time() + requestDTO.getScorePageTime(), expectDelay); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= 9); // 최소 testSecond*3초 지연이 있었는지 확인

        System.out.printf("Expected delay: " + expectDelay + " seconds, Actual delay: %d seconds%n", secondsDifference);

    }

    @DisplayName("라운드 3개는 제대로 작동하나?")
    @Test
    void _test_three_round_takes_32sec() throws ExecutionException, InterruptedException, TimeoutException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        GameStartRequestDTO requestDTO =
                new GameStartRequestDTO("testUser", 1,
                        1, 1, 1, 2, 3, 4);

        //when
        int result = scheduleProvider.startGame(requestDTO.getGameId(), 0
            ).thenCompose(r ->
                    scheduleProvider.roundScheduler(gameId, requestDTO, r + 1)
            ).thenCompose(r ->
                    scheduleProvider.roundScheduler(gameId, requestDTO, r + 1)
            ).thenCompose(r ->
                    scheduleProvider.roundScheduler(gameId, requestDTO, r + 1)
            ).get();
//        log.info(String.valueOf(LocalDateTime.now()));
        LocalDateTime endTime = LocalDateTime.now();
        // 시작과 완료 시각의 차이를 구합니다.
        long secondsDifference = ChronoUnit.SECONDS.between(startTime, endTime);
//        log.info(String.valueOf(secondsDifference));
        int expectDelay = (2 + 3 + 4) * 3 + 5;
        // then
        assertEquals(3, result); // 3라운드까지 잘 카운트가 되엇나?
//        assertEquals(requestDTO.getStage1Time() + requestDTO.getStage2Time() + requestDTO.getScorePageTime(), expectDelay); // 반환된 게임 ID가 일치하는지 확인
        assertTrue(secondsDifference >= expectDelay); // 최소 testSecond*3초 지연이 있었는지 확인

        System.out.printf("Expected delay: " + expectDelay + " seconds, Actual delay: %d seconds%n", secondsDifference);

    }

    @DisplayName("시작 시간 포함 라운드 시간 관리")
    @Test
    public void testGameProcessAsync() throws ExecutionException, InterruptedException {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        AtomicReference<LocalDateTime> asyncEndTime = new AtomicReference<>();
        GameStartRequestDTO requestDTO = new GameStartRequestDTO("testUser", 1, 1, 1, 3, 2, 3, 4);
        int currentRound = 0;
        int roundCount = requestDTO.getRoundCount();
        AtomicBoolean isPass = new AtomicBoolean(false);
        AtomicInteger executedRounds = new AtomicInteger(0);

        // when
        scheduleProvider.startGame(requestDTO.getGameId(), currentRound)
                .thenCompose(v -> {
                    // 초기 체인 생성
                    CompletableFuture<Integer> roundChain = CompletableFuture.completedFuture(currentRound);

                    // 각 라운드에 대한 비동기 작업 체인 구축
                    for (int round = 1; round <= roundCount; round++) {
                        final int currentRoundInLoop = round;
                        roundChain = roundChain.thenCompose(ignored -> {
                            executedRounds.incrementAndGet(); // 라운드 실행 횟수 증가
                            return scheduleProvider.roundScheduler(requestDTO.getGameId(), requestDTO, currentRoundInLoop);
                        });
                        RoundFinishRequestDTO finishRequestDTO = new RoundFinishRequestDTO(requestDTO.getSenderNickname(), requestDTO.getSenderGameId(), requestDTO.getSenderTeamId(), currentRound);
                        gameService.finishRound(finishRequestDTO);
                    }
                    // 마지막 결과를 설정
                    return roundChain;
                }).get();

        log.info(startTime + " Async operation ended at: "+ LocalDateTime.now());
        asyncEndTime.set(LocalDateTime.now());
        long compare = ChronoUnit.SECONDS.between(startTime, asyncEndTime.get());
        long expectedTime = (2 + 3 + 4 + 1) * 3 + 5; // 각 스테이지 시간의 합 (초 단위로 계산) / stage 1, 2, score, wait

        // 비교를 위해 오차 범위 설정
        long tolerance = 1;
        long minTime = expectedTime - tolerance;
        long maxTime = expectedTime + tolerance;

        isPass.set(true);
        assertEquals(roundCount, executedRounds.get(), "roundScheduler 호출 횟수가 예상한 반복 횟수와 다릅니다.");
        assertTrue(compare >= minTime && compare <= maxTime, "scheduler가 예상 시간 범위에서 벗어났습니다. "+ compare +" "+ expectedTime);

    }

    @DisplayName("남은 시간 보내주기")
    @Test
    void _notify_remain_time() throws ExecutionException, InterruptedException {
        int gameId = 1;
        int stageTime = 30;
        int stage = 1;
        LocalDateTime startTime = LocalDateTime.now();
        scheduleProvider.notifyRemainingTime(gameId,stageTime,stage,1, ServerEvent.NOTIFY_LEFT_TIME).get();
        LocalDateTime endTime = LocalDateTime.now();
        log.info(startTime +" : "+endTime);
        long compare = ChronoUnit.SECONDS.between(startTime, endTime);
        assertEquals(compare,30);
    }

}