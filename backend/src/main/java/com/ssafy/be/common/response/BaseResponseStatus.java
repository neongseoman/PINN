package com.ssafy.be.common.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    CHAT_SUCCESS(true, 1100, "채팅 성공"),
    ENTER_SUCCESS(true, 1100, "방 입장 성공"),

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

    CREATE_GAME_ERROR(false, 3100, "방(게임) 생성 중 문제가 발생했습니다."),
    NOT_EXIST_GAME(false, 3101, "존재하지 않는 게임입니다."),
    ALREADY_START_GAME(false, 3102, "게임을 시작한 방입니다. 다른 방을 선택해주세요."),
    NOT_MATCH_PASSWORD(false, 3103, "방 비밀번호를 다시 확인해주세요"),
    NOT_EXIST_UNREADY_TEAM(false, 3104, "모든 팀이 준비 중이라서 방에 들어갈 수 없습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    INTERNAL_SERVER_ERROR(false, 4000, "서버 오류입니다"),
    JSON_PROCESSING_ERROR(false, 4001, "JSON을 처리하는 과정 중 오류가 발생했습니다."),
    FILE_UPLOAD_ERROR(false, 4002, "파일을 업로드 하는 과정 중에 에러가 발생했습니다."),

    /**
     * 5000 : 잡지 못 한 서버 오류
     */
    OOPS(false, 5000, "Oops..."),

    INVALID_CREDENTIAL(false,4003,"AUTH CODE IS INVALID");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
