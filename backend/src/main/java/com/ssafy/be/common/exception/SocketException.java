package com.ssafy.be.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocketException extends RuntimeException{

    public SocketException() {
        System.out.println("SocketException");
    }
}
