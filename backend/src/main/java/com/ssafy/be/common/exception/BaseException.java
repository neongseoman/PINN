package com.ssafy.be.common.exception;

import com.ssafy.be.common.response.BaseResponseStatus;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class BaseException extends RuntimeException{
    private static final Logger log = LoggerFactory.getLogger(BaseException.class);
    private BaseResponseStatus status;
    public BaseException(BaseResponseStatus status) {
        this.status = status;
    }


}