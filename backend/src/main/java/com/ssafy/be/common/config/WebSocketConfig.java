package com.ssafy.be.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;


@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {
    private String EndPoint = "/game";
//    public WebSocketConfig(@Value("${stomp.endpoint}")  String value) {
//        System.out.println(value);
//        this.EndPoint = value;
//    }

    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/game","/team","/guess"); // sub
        config.setApplicationDestinationPrefixes("/app");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(EndPoint).setAllowedOriginPatterns("*");
    }

}

