package com.ssafy.be.common.controller;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MessageTestController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JwtProvider jwtProvider;

    @MessageMapping("/game/test")
    public void test2(@Payload String payload,StompHeaderAccessor accessor){
        GamerPrincipalVO  gamerPrincipalVO = jwtProvider.getGamerPrincipalVOByMessageHeader(accessor);
//        System.out.println(gamerPrincipalVO);
        simpMessagingTemplate.convertAndSend("/game/test",payload);
    }

}
