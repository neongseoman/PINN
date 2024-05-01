package com.ssafy.be.common.component;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameManager{
    // key : game_id
    private ConcurrentHashMap<String, Game> games;

    public ConcurrentHashMap<String, Game> getGames() {
        return games;
    }
    public boolean addGame(String gameId, Game game){
        if (games.getOrDefault(gameId, null) != null){
            games.put(gameId, game);
            return true;
        }
        return false;
    }
}
