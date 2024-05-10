package com.ssafy.be.common.config;

import com.ssafy.be.common.Interceptor.StompInboundMessageInterceptor;
import com.ssafy.be.common.handler.CustomHandshakeHandler;
import com.ssafy.be.common.handler.StompErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;


@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {
    private final StompErrorHandler stompErrorHandler;
    private final StompInboundMessageInterceptor stompInboundMessageInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/game","/team","/guess", "/user"); // sub
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String endPoint = "/game";
        registry.addEndpoint(endPoint).setAllowedOriginPatterns("*");
//        registry.setErrorHandler(stompErrorHandler);
    }

    // 들어오는 메세지나 나가는 메세지의 구독자, 발행자의 상태를 확인하는 시큐리티 작업이 필요할 것 같다.
    // 추후 개발을 할 수 있다면.
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompInboundMessageInterceptor);
    }
}

