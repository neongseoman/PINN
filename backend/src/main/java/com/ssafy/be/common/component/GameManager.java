package com.ssafy.be.common.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
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
    public boolean isGame(Integer gameId) {
        GameComponent gameComponent = games.getOrDefault(gameId, null);
        if (gameComponent == null){
            // 존재하지 않는 게임
            return false;
        } else if (!gameComponent.getStatus().equals("ready")) {
            // 게임 실행 중인 게임
            return false;
        }
        return true;
    }
}
