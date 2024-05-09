package com.ssafy.be.common.handler;


//import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.be.common.exception.BaseException;
import com.ssafy.be.common.exception.SocketException;
import com.ssafy.be.common.model.ExceptionReturn;
import com.ssafy.be.common.response.BaseResponse;
import com.ssafy.be.common.response.BaseResponseStatus;
//import com.ssafy.be.common.utils.HttpResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.Principal;

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
//        log.error("Handle : {}", e.getCause());
//        e.printStackTrace();
        return new BaseResponse<>(BaseResponseStatus.OOPS);
    }

//    @MessageExceptionHandler(BaseException.class)
//    public void StompMessageExceptionHandler(BaseException ex, SocketDTO socketDTO) {
//        log.info("Exception Handler get {} : {} " ,ex ,LocalDateTime.now());
//        sendingOperations.convertAndSend("/user/error/2",ex);
//    }

    @MessageExceptionHandler(BaseException.class)
    public void StompMessageExceptionHandler(BaseException ex) {
        log.error("MessageException : {}, {}", ex.getStatus(),ex.getGamerId());
        ex.printStackTrace(); // 나중에 지우세
        sendingOperations.convertAndSend("/user/error/"+ex.getGamerId(),
                new ExceptionReturn(ex.getStatus().getCode(), ex.getStatus().getMessage()));
    }

//    @MessageExceptionHandler(SocketException.class)
//    public void StompMessageExceptionHandler(SocketException ex) {
//        log.error("MessageException : {}, {}", ex.getStatus(),ex.getGamerId());
//        ex.printStackTrace(); // 나중에 지우세
////        sendingOperations.convertAndSendToUser(principal.getName(),"/user/error/"+ex.getGamerId(),ex.getStatus());
//        sendingOperations.convertAndSend("/user/error/"+ex.getGamerId(),
//                new ExceptionReturn(ex.getStatus().getCode(), ex.getStatus().getMessage()));
//    }


//    @ExceptionHandler(Exception.class)
//    protected BaseResponse<BaseResponseStatus> handleException(AmazonS3Exception e) {
////        log.error("AmazonS3Exception", e);
////        e.printStackTrace();
//        return new BaseResponse<>(BaseResponseStatus.OOPS);
//    }
}
