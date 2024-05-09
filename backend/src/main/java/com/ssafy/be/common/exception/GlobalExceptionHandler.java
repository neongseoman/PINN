package com.ssafy.be.common.exception;


//import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.be.common.model.dto.SocketDTO;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
//import com.ssafy.be.common.utils.HttpResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final SimpMessageSendingOperations sendingOperations;


    @ExceptionHandler(value = JsonProcessingException.class)
    public BaseResponse<BaseResponseStatus> baseException(JsonProcessingException exception) {
//        log.warn("Handle JsonProcessingException : {}", exception.getCause());
//        exception.printStackTrace();
        return new BaseResponse<>(BaseResponseStatus.JSON_PROCESSING_ERROR);
    }

//    @ExceptionHandler(value = MyException.class)
//    public ResponseEntity<HttpResponseBody<?>> catchMyException(MyException e) {
////        log.info(e.getMessage());
////        e.printStackTrace();
//        return new ResponseEntity(new HttpResponseBody<>("FAIL", e.getMessage()), e.getStatus());
//    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse<BaseResponseStatus> baseException(BaseException e) {
//        log.warn("Handle CommonException: {}", e.getStatus());
//        e.printStackTrace();
        return new BaseResponse<>(e.getStatus());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public BaseResponse<BaseResponseStatus> baseException(RuntimeException e) {
//        log.warn("Handle CommonException: {}", e.getCause());
//        e.printStackTrace();
        return new BaseResponse<>(BaseResponseStatus.OOPS);
    }

    @MessageExceptionHandler(BaseException.class)
    public void StompMessageExceptionHandler(SocketException ex, SocketDTO socketDTO) {
        log.info("Exception Handler get {} : {} " ,ex ,LocalDateTime.now());
        sendingOperations.convertAndSend("/game/error/2",ex);
    }

//    @ExceptionHandler(Exception.class)
//    protected BaseResponse<BaseResponseStatus> handleException(AmazonS3Exception e) {
////        log.error("AmazonS3Exception", e);
////        e.printStackTrace();
//        return new BaseResponse<>(BaseResponseStatus.OOPS);
//    }
}