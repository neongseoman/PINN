package com.ssafy.be.common.Provider;


import com.ssafy.be.common.component.GameComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Component
@Log4j2
@RequiredArgsConstructor
public class ScheduleProvider {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    // Publishing을 어디서 할까? Controller? 아님 여기? Controller에서 하는게 맞아 보이긴하는데.
     // 5초 지났고 게임 시작합시다.
    public CompletableFuture<Integer> startGame(int gameId){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() ->{
                log.info("{} at {} is Game Start", LocalDateTime.now(),gameId);
                future.complete(gameId);
        },5, TimeUnit.SECONDS);
        return future;
    }

    public CompletableFuture<Integer> roundStart(int gameId){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() ->{
            log.info("{} at {} is round Start", LocalDateTime.now(),gameId);
            future.complete(gameId);
        },5, TimeUnit.SECONDS);
        return future;
    }

    @Async // stage 1 끝나고 힌트 주기.
    public void sendHint(int gameId,int delayTime, Consumer<Integer> onComplete){
        executorService.schedule(() ->{
            log.info("{} at {} receive Hint", LocalDateTime.now(),gameId);
            onComplete.accept(gameId);
        },delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.
    }

    // 스테이지 2까지 끝나고 라운드 종료 -> 결산 페이지로 넘어가세요.
    // GameComponent에 있는 모든 팀들의 pin을 정산해서 점수로 환산함.
    @Async
    public void roundEnd(int gameId, int delayTime, Consumer<Integer> onComplete, GameComponent gameComponent){
        executorService.schedule(() ->{
            // 점수 계산하기
//            List<Integer> result = gameComponent.getTeams().entrySet().stream().map(r -> r.getValue().)
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
