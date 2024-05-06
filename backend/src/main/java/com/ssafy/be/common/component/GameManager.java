package com.ssafy.be.common.component;

import static com.ssafy.be.common.response.BaseResponseStatus.ALREADY_START_GAME;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_EXIST_GAME;
import static com.ssafy.be.common.response.BaseResponseStatus.NOT_MATCH_PASSWORD;

import com.ssafy.be.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Log4j2
public class GameManager {
    // key : game_id
    private ConcurrentHashMap<Integer, GameComponent> games;

    public GameManager() {
        this.games = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer, GameComponent> getGames() {
        return games;
    }
    public boolean addGame(GameComponent game){
        if (games.getOrDefault(game.getGameId(), null) == null){
            games.put(game.getGameId(), game);
            return true;
        }
        return false;
    }

    /*
    * gameId가 GameManager에 존재하는 게임인지 확인하는 메서드
    * */
    public boolean isGame(Integer gameId, String password) {
        GameComponent gameComponent = games.getOrDefault(gameId, null);
//        log.info("server password" + gameComponent.getPassword());
        if (gameComponent == null){
            // 존재하지 않는 게임
            throw new BaseException(NOT_EXIST_GAME);
        }
        if (!gameComponent.getStatus().equals(GameStatus.READY)) {
            // 게임 실행 중인 게임
            throw new BaseException(ALREADY_START_GAME);
        }
        if (gameComponent.getPassword() != null){
            // 비밀번호가 있는 방
            if(password == null || !password.equals(gameComponent.getPassword())){
                throw new BaseException(NOT_MATCH_PASSWORD);
            }
        }
        return true;
    }
}
