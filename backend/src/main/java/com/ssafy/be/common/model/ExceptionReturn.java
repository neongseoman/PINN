package com.ssafy.be.common.model;

import lombok.Getter;

@Getter
public class ExceptionReturn {
    int code;
    String String;

    public ExceptionReturn(int code, String string) {
        this.code = code;
        this.String = string;
    }
}