package com.ssafy.be.common.Provider;


import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.exception.SocketException;
import com.ssafy.be.common.model.dto.ServerEvent;
import com.ssafy.be.common.model.dto.ServerSendEvent;
import com.ssafy.be.game.model.dto.GameStartRequestDTO;
import com.ssafy.be.game.model.vo.GameInitVO;
import com.ssafy.be.game.model.vo.RoundInitVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Component
@Log4j2
@RequiredArgsConstructor
public class ScheduleProvider {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final SimpMessageSendingOperations sendingOperations;


    // 5초 지났고 게임 시작합시다.
    public CompletableFuture<Integer> startGame
    (int gameId, GameInitVO gameInitVO) throws SocketException, BaseException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
            future.complete(gameId);
        },5, TimeUnit.SECONDS);
        return future;
    }
    // Stage 1으로 진입하는 것.
    public CompletableFuture<Integer> roundStart(int gameId, int delayTime) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
//            log.info("Round start: {}  at {}",gameId, LocalDateTime.now());
////            RoundInitVO r
//            ServerSendEvent serverMsg = new ServerSendEvent(ServerEvent.ROUND_START);
//            sendingOperations.convertAndSend("/game/" + gameId, serverMsg);
            // round 정보를 보내줘야함
            future.complete(gameId);
        }, delayTime, TimeUnit.SECONDS);
        return future;
    }

    // Hint를 제공함.
    // Stage 2로 진입.
    public CompletableFuture<Integer> sendHint(int gameId, int delayTime) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
//            log.info("Hint send: {}  at {}",  gameId, LocalDateTime.now());
//            ServerSendEvent serverMsg = new ServerSendEvent(ServerEvent.HINT);
//            sendingOperations.convertAndSend("/game/" + gameId, serverMsg);
            future.complete(gameId);
        }, delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.
        return future;
    }

    // 스테이지 2까지 끝나고 라운드 종료 -> 점수 페이지로 넘어가세요.
    // GameComponent에 있는 모든 팀들의 pin을 정산해서 점수로 환산함.
    public CompletableFuture<Integer> roundEnd(int gameId, int delayTime) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
            log.trace("{} at {} Round is Over", LocalDateTime.now(), gameId);
            future.complete(gameId);
        }, delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.
        return future;
    }

    // 결산 페이지 끝났고 다음 라운드로 넘어가세요.
    // => Rount Start와 같은 기능인데 구분할 필요가 있을까?
    public CompletableFuture<Integer> nextRound(int gameId, int delayTime) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
            log.trace("{} at {} go to Next Round", LocalDateTime.now(), gameId);
            future.complete(gameId);
        }, delayTime, TimeUnit.SECONDS); // 게임 시간 인자로 받으면 수정할 수 있음.
        return future;
    }

    // 게임 정산 페이지로 가세요.
    public CompletableFuture<Integer> endGame(int gameId, int delayTime) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
            log.trace("{} at {} is Game End", LocalDateTime.now(), gameId);
            future.complete(gameId);
        }, delayTime, TimeUnit.SECONDS);
        return future;
    }

    public CompletableFuture<Integer> goToRoom(int gameId, int delayTime) {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executorService.schedule(() -> {
            log.trace("{} at {} is go To Room", LocalDateTime.now(), gameId);
            future.complete(gameId);
        }, delayTime, TimeUnit.SECONDS);
        return future;
    }

    //TODO : 나중에 수정하고 싶지만 지금은 시간이 없어서 봐준다.
//    public CompletableFuture<Integer> gameProgress(int gameId,int progress){
//
//    }
//
//    public CompletableFuture<GameStartRequestDTO> startGame(int gameId, GameStartRequestDTO gameStartRequestDTO){
//        CompletableFuture<GameStartRequestDTO> future = new CompletableFuture<>();
//        executorService.schedule(() ->{
//            log.trace("{} at {} is Game Start", LocalDateTime.now(),gameId);
//            future.complete(gameStartRequestDTO);
//        },5, TimeUnit.SECONDS);
//        return future;
//    }
}
