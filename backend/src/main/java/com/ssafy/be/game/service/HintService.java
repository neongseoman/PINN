package com.ssafy.be.game.service;


import com.ssafy.be.game.model.WeatherAPIResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;


@RequiredArgsConstructor
@Service
@Log4j2
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
//                .filter(logRequest())
                .build();
    }
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {}", clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    public void fetchWeatherDataWithMonth(float lat, float lon) {
        log.info("API Start Time : {}", LocalDateTime.now());

        Flux<Integer> months = Flux.range(1, 12);
        Flux<WeatherAPIResponseDTO> response = months.flatMap(month ->webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/month")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("month", month)
                        .queryParam("appid", weatherAPI)
                        .build())
                .retrieve()
                .bodyToFlux(WeatherAPIResponseDTO.class));

        response.subscribe(
                data -> {
                    System.out.println("Received Data : " + data.getResult());
                    log.info("API End Time : {}", LocalDateTime.now());
                },
                error -> {
                    System.out.println("Error occurred : " + error.getMessage());
                },
                () -> System.out.println("Request complete"));
    }
}
