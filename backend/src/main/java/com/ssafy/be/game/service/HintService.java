package com.ssafy.be.game.service;


import com.ssafy.be.game.model.WeatherAPIResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class HintService {
    @Value("${weather_map.url}")
    private String weatherURL;

    @Value("${weather_map.api}")
    private String weatherAPI;
    private WebClient webClient;

    @PostConstruct
    private void init() {
        webClient = WebClient.builder()
                .baseUrl(weatherURL)
                .build();
    }

    public void fetchWeatherDataWithMonth(float lat, float lon) {
        Mono<WeatherAPIResponseDTO> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/month")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("month",2)
                        .queryParam("appid", weatherAPI)
                        .build())
                .exchangeToMono(clientResponse -> {
                    // 요청 및 응답 정보 기록
                    System.out.println("Request URL: " + clientResponse.request().getURI());
                    System.out.println("Request Method: " + clientResponse.request().getMethod());
                    System.out.println("Request Headers: " + clientResponse.request().getHeaders());

                    return clientResponse.bodyToMono(WeatherAPIResponseDTO.class);
                });

        response.subscribe(
                data -> {
                    System.out.println("Received Data : " + data);
                    System.out.println(data.getResult());
                },
                error -> {
                    System.out.println("Error occurred : " + error.getMessage());
                    System.out.println(Arrays.toString(error.getStackTrace()));
                    System.out.println(error.getCause());
                },
                () -> System.out.println("Request complete"));

    }
}
