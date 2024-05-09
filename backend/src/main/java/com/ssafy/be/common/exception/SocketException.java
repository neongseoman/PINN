package com.ssafy.be.common.exception;

import com.ssafy.be.common.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class SocketException extends BaseException{
    private int gamerId;
    public SocketException(BaseResponseStatus status, int gamerId) {
        super(status);
        this.gamerId = gamerId;

    }
}
