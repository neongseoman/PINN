package com.ssafy.be.common.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    SUCCESS_DELETE_MOKKOJI(true, 1100, "정상적으로 삭제했습니다"),
    SUCCESS_KICK_MOKKOJI_MEMBER(true, 1101, "회원을 정상적으로 강퇴했습니다"),
    SUCCESS_CREATE_MOKKOJI(true, 1102, "정상적으로 생성했습니다"),
    SUCCESS_LEAVE_MOKKOJI(true, 1103, "정상적으로 떠났습니다"),


    SUCCESS_CREATE_TOKEN(true, 1200, "토큰 발급에 성공했습니다."),


    SUCCESS_CREATE_PRODUCT(true,1401, "상품 추가에 성공했습니다."),

    /**
     * 2000 : Request 오류
     */
    EMPTY_SIGN(false, 2000, "sign 값이 없습니다."),

    /* User */
    ALREADY_EXIST_USER(false, 2034, "이미 존재하는 회원입니다."),


    /**
     * 3000 : Response 오류
     */
    VALIDATED_ERROR(false, 3000, "VALIDATED_ERROR"), // @Valid 예외 처리
    SEND_MESSAGE_ERROR(false, 3001, "메시지를 발송하는 과정 중 오류가 발생했습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    INTERNAL_SERVER_ERROR(false, 4000, "서버 오류입니다"),
    JSON_PROCESSING_ERROR(false, 4001, "JSON을 처리하는 과정 중 오류가 발생했습니다."),
    FILE_UPLOAD_ERROR(false, 4002, "파일을 업로드 하는 과정 중에 에러가 발생했습니다."),

    /**
     * 5000 : 잡지 못 한 서버 오류
     */
    OOPS(false, 5000, "Oops...");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
