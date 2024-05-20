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

    // LOBBY
    CREATE_GAME_ERROR(false, 3100, "방(게임) 생성 중 문제가 발생했습니다."),
    NOT_EXIST_GAME(false, 3101, "존재하지 않는 게임입니다."),
    ALREADY_START_GAME(false, 3102, "게임을 시작한 방입니다. 다른 방을 선택해주세요."),
    NOT_MATCH_PASSWORD(false, 3103, "방 비밀번호를 다시 확인해주세요"),
    NOT_EXIST_UNREADY_TEAM(false, 3104, "모든 팀이 준비 중이라서 방에 들어갈 수 없습니다."),
    FULL_ROOM_ERROR(false, 3105, "방 수용 인원을 초과합니다."),
    NOT_EXIST_VALIE_TEAM(false,3106, "참여할 수 있는 팀이 없습니다."),

    //ROOM
    FULL_TEAM_ERROR(false, 3150, "팀 수용 인원을 초과합니다."),
    NOT_EXIST_GAMER(false, 3151, "해당 게임에 존재하지 않는 사용자입니다."),
    NOT_EXIST_LEADER(true, 3152, "마지막 사람이 방을 나갑니다. 방을 삭제하였습니다."),
    NOT_EXIST_TEAM(false, 3153, "존재하지 않는 팀입니다."),


    // GAME
    NOT_STARTED_GAME(false, 3201, "게임이 시작되지 않은 방이므로 초기화 작업을 수행할 수 없습니다."),
    ALREADY_GUESSED_TEAM(false, 3202, "이미 해당 라운드에 guess를 완료한 팀입니다."),
    NOT_EXIST_READY_GAME(false,3202,"빨리 시작할 수 있는 게임이 없습니다."),
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
