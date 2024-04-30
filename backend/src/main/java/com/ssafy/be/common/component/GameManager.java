package com.ssafy.be.common.component;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameManager{
    ConcurrentHashMap<String, Game> games;


    public ConcurrentHashMap<String, Game> getGames() {
        return games;
    }
}
