package com.recipia.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀한 에러코드를 작성한다.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 로그인 관련 에러
    USER_NOT_FOUND(404, "1001", "유저를 찾을 수 없습니다."),
    INVALID_PASSWORD(404, "1002", "패스워드가 틀렸습니다."),
    USERNAME_PASSWORD_AUTHENTICATION_TOKEN_ERROR(404, "1003", "attemptAuthentication 인증 에러"),

    // JWT 관련 에러
    INVALID_JSON_WEB_TOKEN(401, "1101", "토큰이 유효하지 않습니다."),
    JWT_TOKEN_EXPIRED(401, "1102", "JWT 토큰이 만료되었습니다."),
    JWT_TOKEN_INVALID(401, "1103", "JWT 토큰이 유효하지 않습니다."),
    JWT_TOKEN_MISSING(401, "1104", "JWT 토큰이 누락되었습니다."),

    // 회원가입 관련 에러
    EMAIL_ALREADY_EXISTS(400, "1201", "이미 존재하는 이메일입니다."),
    USERNAME_ALREADY_EXISTS(400, "1202", "이미 존재하는 사용자 이름입니다."),
    ACCOUNT_LOCKED(401, "1203", "계정이 잠겼습니다."),

    // DB 관련 에러
    REQUIRED_FIELD_NULL(404, "8001", "필수 컬럼값이 존재하지 않습니다."),
    FILE_NOT_FOUND(404, "8002", "파일 경로에 파일이 존재하지 않습니다."),
    FILE_DUPLICATED(404, "8003", "중복된 파일입니다."),
    DB_ERROR(500, "8004", "DB ERROR"),

    // 공통 에러
    IO_ERROR(404, "9005", "INPUT/OUTPUT ERROR"),
    BAD_REQUEST(400, "4000", "잘못된 요청"),

    // 추가된 특정 에러 코드 (예시)
    INTERNAL_SERVER_ERROR(500, "5000", "서버 내부 오류"),
    NULL_POINTER_EXCEPTION(500, "5001", "Null 참조 오류"),
    ILLEGAL_ARGUMENT_EXCEPTION(400, "4001", "부적절한 인자 오류"),


    // 외부 서비스 에러
    RECIPE_SERVICE_ERROR(500, "9002", "RECIPE 서비스 에러"),
    WRIGGLE_SERVICE_ERROR(500, "9003", "WRIGGLE 서비스 에러"),
    CHAT_SERVICE_ERROR(500, "9004", "CHAT 서비스 에러");

    private final int status;
    private final String code;
    private final String message;

}

