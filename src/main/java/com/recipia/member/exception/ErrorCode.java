package com.recipia.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀한 에러코드를 작성한다.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 로그인
    // 1.0
    USER_NOT_FOUND(404, "1001", "유저를 찾을수 없습니다."),
    INVALID_PASSWORD(404, "1002", "패스워드가 틀렸습니다."),
    //    INVALID_PERMISSION(404, "1003", "접근권한이 없습니다."),

    // JWT
    // 1.1
    INVALID_JSON_WEB_TOKEN(401, "1101", "토큰이 유효하지 않습니다."),

    // 회원가입
    // 1.2
    //    INVALID_EMAIL(404, "1201", "토큰이 유효하지 않습니다."),
    //    INVALID_TOKEN(404, "1202", "토큰이 유효하지 않습니다."),
    //    INVALID_TOKEN(404, "1203", "토큰이 유효하지 않습니다."),

    // DB
    // 8.0
    REQUIRED_FIELD_NULL(404, "8001", "필수 컬럼값이 존재하지 않습니다."),
    FILE_NOT_FOUND(404, "8002", "파일 경로에 파일이 존재하지 않습니다."),
    FILE_DUPLICATED(404, "8003", "중복된 파일입니다."),
    DB_ERROR(500, "8004", "DB ERROR"),

    // EXTERNAL 9.0
    RECIPE_SERVICE_ERROR(500, "9002", "RECIPE 서비스 에러"),
    WRIGGLE_SERVICE_ERROR(500, "9003", "WRIGGLE 서버스 에러"),
    CHAT_SERVICE_ERROR(500, "9004", "CHAT 서버스 에러")

    ;

    private int status;
//    private HttpStatus status;
    private String code;
    private String message;

}

