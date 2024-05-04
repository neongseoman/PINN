package com.ssafy.be.game.service;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("WeatherAPI Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Allows non-static @BeforeAll
class HintServiceTest {

    @Value("${weather_map.url}")
    private String weatherURL;

    @Value("${weather_map.api}")
    private String weatherAPI;

    private WebClient webClient;

    @BeforeAll
    void init() {
        webClient = WebClient.builder()
                .baseUrl(weatherURL)
                .build();
    }

    @Nested
    @DisplayName("fetch Weather Data Test")
    class FetchWeatherDataTest {
        @Test
        void testFetchWeatherDataWithMonth() {
            String lat = "35";
            String lon = "139";
            Mono<String> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/month")
                            .queryParam("lat", lat)
                            .queryParam("lon", lon)
                            .queryParam("appid", weatherAPI)
                            .build())
                    .retrieve().bodyToMono(String.class);

            response.subscribe(
                    data -> assertEquals("Expected Data", data),
                    error -> fail("API call failed with error: " + error.getMessage()),
                    () -> System.out.println("Request complete"));
        }
    }
}