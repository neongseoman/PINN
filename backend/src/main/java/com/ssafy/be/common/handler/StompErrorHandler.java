package com.ssafy.be.common.handler;

import com.ssafy.be.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Log4j2
@RequiredArgsConstructor
@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {


    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]>clientMessage, Throwable ex)
    {
        Throwable exception = new Exception("abc");
        return handleUnauthorizedException(clientMessage, exception);

    }

    private Message<byte[]> handleUnauthorizedException(Message<byte[]> clientMessage, Throwable ex)
    {
        return prepareErrorMessage(clientMessage, ex.getMessage(), "1202");

    }

    private Message<byte[]> prepareErrorMessage(Message<byte[]> clientMessage,String message , String errorCode)
    {


        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);

        accessor.setMessage(errorCode);
        accessor.setLeaveMutable(true);

        return MessageBuilder.createMessage(message.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
    }
}
