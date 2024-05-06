package com.ssafy.be.common.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.game.service.HintService;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class TestController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtProvider jwtProvider;
    private final HintService hintService;

    @MessageMapping("/game/test")
    @SendTo("/game/to")
    public void test2(@Payload String payload, StompHeaderAccessor accessor) {
        GamerPrincipalVO gamerPrincipalVO = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor);
//        System.out.println(gamerPrincipalVO);
        simpMessagingTemplate.convertAndSend("/game/test", payload);
    }

    @GetMapping("/game/test01")
    public void test01(ServletRequest req) {
        GamerPrincipalVO gamerPrincipalVO  = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        System.out.println(gamerPrincipalVO);
    }

    @GetMapping("/game/monthApiTest")
    public void testMonthApi(ServletRequest req) {
        GamerPrincipalVO gamerPrincipalVO  = (GamerPrincipalVO) req.getAttribute("gamerPrincipal");
        log.info("Weather Month API Test");
        hintService.fetchWeatherDataWithMonth(35,139);

    }
}
