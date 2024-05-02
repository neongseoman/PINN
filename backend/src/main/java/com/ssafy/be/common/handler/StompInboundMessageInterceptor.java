package com.ssafy.be.common.handler;

import com.ssafy.be.auth.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompInboundMessageInterceptor implements ChannelInterceptor {
    private final JwtProvider jwtProvider;
    //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/support/ChannelInterceptor.html
    // Message가 channel에 들어오기 전에 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
//        System.out.println(headerAccessor.getMessageHeaders());
//        System.out.println("full message:" + message);
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            log.info(headerAccessor.getSessionId() + " connected");
        }
        if (StompCommand.MESSAGE.equals(headerAccessor.getCommand())){
            System.out.println("auth:" + headerAccessor.getNativeHeader("auth header"));
        }
//        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
//            System.out.println("msg: " + "conne");
//        }
        return message;
    }


}
