package com.ssafy.be.game;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class testcontroller {
//    private final SimpleMessageConverter simpleMessageConverter;
    @MessageMapping("/game/test01")
    public void test01(@Payload String game) {
        System.out.println(game);
//        simpleMessageConverter.
    }
}
