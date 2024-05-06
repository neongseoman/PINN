package com.ssafy.be.game;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class HintService {
    @Value("${weather_map.url}")
    private String weatherURL;
    @Value("${weather_map.api}")
    private String weatherAPI;
    WebClient weatherClient = WebClient.builder()
            .build();

//    public
}
