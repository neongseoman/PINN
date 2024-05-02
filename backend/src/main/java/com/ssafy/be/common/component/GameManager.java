package com.ssafy.be.common.component;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameManager {
    // key : game_id
    private ConcurrentHashMap<Integer, GameComponent> games;

    public ConcurrentHashMap<Integer, GameComponent> getGames() {
        return games;
    }

    public boolean addGame(Integer gameId, GameComponent game) {
        if (games.getOrDefault(gameId, null) != null) {
            games.put(gameId, game);
            return true;
        }
        return false;
    }
}
