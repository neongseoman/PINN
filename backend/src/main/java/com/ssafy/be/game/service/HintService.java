package com.ssafy.be.game.service;

import com.ssafy.be.game.model.dto.hint.WeatherAPIResponseDTO;
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
import java.util.Comparator;
import java.util.List;


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

    public  Mono<List<WeatherAPIResponseDTO>> fetchWeatherDataWithMonth(float lat, float lon) {
        log.info("API Start Time : {}", LocalDateTime.now());
        Flux<Integer> months = Flux.range(1, 12);
        Mono<List<WeatherAPIResponseDTO>> responses = months.flatMap(month -> webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/month")
                                .queryParam("lat", lat)
                                .queryParam("lon", lon)
                                .queryParam("month", month)
                                .queryParam("appid", weatherAPI)
                                .build())
                        .retrieve()
                        .bodyToMono(WeatherAPIResponseDTO.class))
                .collectList()  // 모든 결과를 리스트로 모음
                .doOnSuccess(dataList -> {
                    System.out.println("Received Data: " + dataList);
                    log.info("API End Time : {}", LocalDateTime.now());
                })
                .doOnError(error -> {
                    System.out.println("Error occurred: " + error.getMessage());
                });
        return responses;


    }
    public Mono<Double> getAnnualTemperatureRange( Mono<List<WeatherAPIResponseDTO>> weatherDat) {
        Mono<Double> annualTemperatureRange = weatherDat.map(dataList -> {
            double highest = dataList.stream().map(data -> data.getResult().getTemp().getRecordMax())
                    .max(Comparator.naturalOrder()).orElse(Double.NEGATIVE_INFINITY);

            double lowestTemperature = dataList.stream()
                    .map(weatherData -> weatherData.getResult().getTemp().getRecordMin())
                    .min(Comparator.naturalOrder())
                    .orElse(Double.POSITIVE_INFINITY);

            return highest - lowestTemperature;
        });
        return annualTemperatureRange;
    }
}
