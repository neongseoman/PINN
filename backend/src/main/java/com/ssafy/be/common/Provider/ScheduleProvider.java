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
                log.trace("{} at {} is Game Start", LocalDateTime.now(),gameId);
                future.complete(gameId);
        },5, TimeUnit.SECONDS);
        return future;
    }

    // Stage 1으로 진입하는 것.
    public CompletableFuture<Integer> roundStart(int gameId,int delayTime){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() ->{
            log.trace("{} at {} is round Start", LocalDateTime.now(),gameId);
            future.complete(gameId);
        },delayTime, TimeUnit.SECONDS);
        return future;
    }

    // Hint를 제공함.
    // Stage 2로 진입.
    public CompletableFuture<Integer> sendHint(int gameId,int delayTime){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() ->{
            log.trace("{} at {} receive Hint", LocalDateTime.now(),gameId);
            future.complete(gameId);
        },delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.
        return future;
    }

    // 스테이지 2까지 끝나고 라운드 종료 -> 점수 페이지로 넘어가세요.
    // GameComponent에 있는 모든 팀들의 pin을 정산해서 점수로 환산함.
    public  CompletableFuture<Integer> roundEnd(int gameId, int delayTime){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() ->{
            log.trace("{} at {} Round is Over", LocalDateTime.now(),gameId);
            future.complete(gameId);
        },delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.
        return future;
    }

    // 결산 페이지 끝났고 다음 라운드로 넘어가세요.
    // => Rount Start와 같은 기능인데 구분할 필요가 있을까?
    public CompletableFuture<Integer> nextRound(int gameId,int delayTime){
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() ->{
            log.trace("{} at {} go to Next Round", LocalDateTime.now(),gameId);
            future.complete(gameId);
        },delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.
        return future;
    }

}
