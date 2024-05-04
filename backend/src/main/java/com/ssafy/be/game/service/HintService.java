package com.ssafy.be.game.service;


import com.ssafy.be.game.model.WeatherAPIResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
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
                        .queryParam("appid", weatherAPI)
                        .build())
                .retrieve().bodyToMono(WeatherAPIResponseDTO.class);

        response.subscribe(
                data -> {
                    System.out.println("Received Data : " + data);
                    // 추가적으로 data 객체를 사용하여 필요한 정보를 처리
                    System.out.println("Temperature Mean: " + data.getResult().getTemp().getMean());
                },
                error -> System.out.println("Error occurred : " + error.getMessage()),
                () -> System.out.println("Request complete"));

    }
}
