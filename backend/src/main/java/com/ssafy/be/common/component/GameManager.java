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
}
