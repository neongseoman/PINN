package com.ssafy.be.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Log4j2
@RequiredArgsConstructor
public class testController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/game/test")
    public void test2(@Payload String payload, SimpMessageHeaderAccessor accessor){
        simpMessagingTemplate.convertAndSend("/game/test",payload);
    }

}
