package com.ssafy.be.common.exception;

import com.ssafy.be.common.response.BaseResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException{
    private BaseResponseStatus status;
    public BaseException(BaseResponseStatus status) {
        this.status = status;
    }

}