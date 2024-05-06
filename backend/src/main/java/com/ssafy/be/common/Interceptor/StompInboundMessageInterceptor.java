package com.ssafy.be.common.Interceptor;

import com.ssafy.be.auth.jwt.JwtProvider;
import com.ssafy.be.gamer.model.GamerPrincipalVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
@Log4j2
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompInboundMessageInterceptor implements ChannelInterceptor {
    private final JwtProvider jwtProvider;
    //https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/support/ChannelInterceptor.html
    // Message가 channel에 들어오기 전에 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        log.info(headerAccessor.toString());

        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            log.info(headerAccessor.getSessionId() + " connected");
        }
        if (StompCommand.SEND.equals(headerAccessor.getCommand())){
            String token = headerAccessor.getNativeHeader("Auth").get(0);
            UsernamePasswordAuthenticationToken authentication = jwtProvider.getAuthentication(token);
        }
//        if (StompCommand.ABORT.equals(headerAccessor.getCommand())){
//            log.info("Connected is ABORT");
//        }
//        if (StompCommand.ERROR.equals(headerAccessor.getCommand())){
//            log.info("Connected is ERROR");
//        }
//        if (StompCommand.UNSUBSCRIBE.equals(headerAccessor.getCommand())){
//            log.info("Connected is UNSUBSCRIBE");
//        }
//        if (StompCommand.DISCONNECT.equals(headerAccessor.getCommand())){
//            log.info("Connected is Disconnected");
//        }
        return message;
    }


}
