//package com.ssafy.be.game.service;
//
//import java.io.IOException;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.stream.Stream;
//
//import com.ssafy.be.game.model.WeatherAPIResponseDTO;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.client.MockRestServiceServer;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import org.springframework.http.client.reactive.ClientHttpConnector;
//import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
//import org.springframework.http.client.reactive.JdkClientHttpConnector;
//import org.springframework.http.client.reactive.JettyClientHttpConnector;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Named.named;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
//
//
//@Nested
//@DisplayName("fetch Weather Data Test")
//@SpringBootTest
//class HintServiceTest {
//    @Autowired
//    private HintService hintService;
//    @Value("${weather_map.url}")
//    private String weatherURL;
//
//    @Value("${weather_map.api}")
//    private String weatherAPI;
//    private final float testLat = 35.0f;
//    private final float testLon = 139.0f;
//
//    @DisplayName("Weather MAP MONTH API Test")
//    @Test
//    void _test_weather_map_month_api() {
//        //given
//        String month = "2";
//        String url = weatherURL + "/month";
////        mockServer.expect(requestTo(url))
////                .andRespond(withSuccess(expectMonthData, MediaType.APPLICATION_JSON));
//
//        hintService.fetchWeatherDataWithMonth(testLat,testLon);
//
//    }
//
//
//    private String expectMonthData = "{\n" +
//            "    \"cod\": 200,\n" +
//            "    \"city_id\": 1856977,\n" +
//            "    \"calctime\": 1.240081505,\n" +
//            "    \"result\": {\n" +
//            "        \"month\": 2,\n" +
//            "        \"temp\": {\n" +
//            "            \"record_min\": 258.03,\n" +
//            "            \"record_max\": 297.15,\n" +
//            "            \"average_min\": 266.88,\n" +
//            "            \"average_max\": 292.05,\n" +
//            "            \"median\": 278.6,\n" +
//            "            \"mean\": 278.05,\n" +
//            "            \"p25\": 274.73,\n" +
//            "            \"p75\": 281.94,\n" +
//            "            \"st_dev\": 5.76,\n" +
//            "            \"num\": 8136\n" +
//            "        },\n" +
//            "        \"pressure\": {\n" +
//            "            \"min\": 915,\n" +
//            "            \"max\": 1037,\n" +
//            "            \"median\": 1014,\n" +
//            "            \"mean\": 1004.83,\n" +
//            "            \"p25\": 998,\n" +
//            "            \"p75\": 1020,\n" +
//            "            \"st_dev\": 25.99,\n" +
//            "            \"num\": 8136\n" +
//            "        },\n" +
//            "        \"humidity\": {\n" +
//            "            \"min\": 2,\n" +
//            "            \"max\": 100,\n" +
//            "            \"median\": 73,\n" +
//            "            \"mean\": 71.57,\n" +
//            "            \"p25\": 55,\n" +
//            "            \"p75\": 92,\n" +
//            "            \"st_dev\": 21.72,\n" +
//            "            \"num\": 8079\n" +
//            "        },\n" +
//            "        \"wind\": {\n" +
//            "            \"min\": 0,\n" +
//            "            \"max\": 19,\n" +
//            "            \"median\": 1.57,\n" +
//            "            \"mean\": 2.37,\n" +
//            "            \"p25\": 0.98,\n" +
//            "            \"p75\": 3.1,\n" +
//            "            \"st_dev\": 2.16,\n" +
//            "            \"num\": 8136\n" +
//            "        },\n" +
//            "        \"precipitation\": {\n" +
//            "            \"min\": 0,\n" +
//            "            \"max\": 3,\n" +
//            "            \"median\": 0,\n" +
//            "            \"mean\": 0.08,\n" +
//            "            \"p25\": 0,\n" +
//            "            \"p75\": 0,\n" +
//            "            \"st_dev\": 0.34,\n" +
//            "            \"num\": 8136\n" +
//            "        },\n" +
//            "        \"clouds\": {\n" +
//            "            \"min\": 0,\n" +
//            "            \"max\": 100,\n" +
//            "            \"median\": 56,\n" +
//            "            \"mean\": 50.12,\n" +
//            "            \"p25\": 8,\n" +
//            "            \"p75\": 88,\n" +
//            "            \"st_dev\": 37.98,\n" +
//            "            \"num\": 8136\n" +
//            "        },\n" +
//            "        \"sunshine_hours\": 46.42\n" +
//            "    }\n" +
//            "}";
//}
