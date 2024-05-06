package com.ssafy.be.common.Provider;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Component
@Log4j2
@RequiredArgsConstructor
public class ScheduleProvider {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    // Publishing을 어디서 할까? Controller? 아님 여기? Controller에서 하는게 맞아 보이긴하는데.
    @Async // 5초 지났고 게임 시작합시다.
    public void startGame(int gameId, Consumer<Integer> onComplete){
        ScheduledFuture<?> future = executorService.schedule(() ->{
                log.info("{} at {} is Game Start", LocalDateTime.now(),gameId);
                onComplete.accept(gameId);
        },5, TimeUnit.SECONDS);
    }

    @Async // stage 1 끝나고 힌트 주기.
    public void sendHint(int gameId,int delayTime, Consumer<Integer> onComplete){
        executorService.schedule(() ->{
            log.info("{} at {} receive Hint", LocalDateTime.now(),gameId);
            onComplete.accept(gameId);
        },delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.

    }

    @Async // 스테이지 2까지 끝나고 라운드 종료 -> 결산 페이지로 넘어가세요.
    public void roundEnd(int gameId,int delayTime, Consumer<Integer> onComplete){
        executorService.schedule(() ->{
            log.info("{} at {} Round is Over", LocalDateTime.now(),gameId);
            onComplete.accept(gameId);
        },delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.

    }

    @Async // 결산 페이지 끝났고 다음 라운드로 넘어가세요.
    public void nextRound(int gameId,int delayTime, Consumer<Integer> onComplete){
        executorService.schedule(() ->{
            log.info("{} at {} go to Next Round", LocalDateTime.now(),gameId);
            onComplete.accept(gameId);
        },delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.
    }

}
