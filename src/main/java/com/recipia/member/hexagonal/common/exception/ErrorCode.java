package com.recipia.member.hexagonal.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀한 에러코드를 작성한다.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 로그인 관련 에러
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "유저를 찾을 수 없습니다."),

    // JWT 관련 에러
    INVALID_JWT(401, "INVALID_JWT", "토큰이 유효하지 않습니다."),
    EXPIRED_JWT(401, "EXPIRED_JWT", "JWT 토큰이 만료되었습니다."),
    MISSING_JWT(401, "MISSING_JWT", "JWT 토큰이 누락되었습니다."),

    // 회원가입 관련 에러
    EMAIL_ALREADY_EXISTS(400, "EMAIL_ALREADY_EXISTS", "이미 존재하는 이메일입니다."),
    USERNAME_ALREADY_EXISTS(400, "USERNAME_ALREADY_EXISTS", "이미 존재하는 사용자 아이디입니다."),
    ACCOUNT_LOCKED(401, "ACCOUNT_LOCKED", "계정이 잠겼습니다."),

    // DB 관련 에러
    REQUIRED_FIELD_NULL(404, "REQUIRED_FIELD_NULL", "필수 컬럼값이 존재하지 않습니다."),
    FILE_NOT_FOUND(404, "FILE_NOT_FOUND", "파일 경로에 파일이 존재하지 않습니다."),
    FILE_DUPLICATED(404, "FILE_DUPLICATED", "중복된 파일입니다."),
    DB_ERROR(500, "DB_ERROR", "DB ERROR"),

    // Zipkin 관련 에러
    NO_TRACE_ID_IN_MESSAGE(404, "NO_TRACE_ID_IN_MESSAGE", "message에 traceid가 없습니다."),

    // 공통 에러
    IO_ERROR(404, "IO_ERROR", "INPUT/OUTPUT ERROR"),
    BAD_REQUEST(400, "BAD_REQUEST", "잘못된 요청"),
    FILTER_USERNAME_PASSWORD_AUTHENTICATION_TOKEN(404, "FILTER_USERNAME_PASSWORD_AUTHENTICATION_TOKEN", "filter attemptAuthentication 인증 에러"),
    EVENT_NOT_FOUND(404, "EVENT_NOT_FOUND", "이벤트 저장소에 해당 이벤트를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 내부 오류"),
    NULL_POINTER_EXCEPTION(500, "NULL_POINTER_EXCEPTION", "Null 참조 오류"),
    ILLEGAL_ARGUMENT_EXCEPTION(400, "ILLEGAL_ARGUMENT_EXCEPTION", "부적절한 인자 오류"),

    AWS_SNS_CLIENT(500, "AWS_SNS_CLIENT", "SNS 발행 에러"),

    // 외부 서비스 에러
    RECIPE_SERVICE_ERROR(500, "RECIPE_SERVICE_ERROR", "RECIPE 서비스 에러"),
    WRIGGLE_SERVICE_ERROR(500, "WRIGGLE_SERVICE_ERROR", "WRIGGLE 서비스 에러"),
    CHAT_SERVICE_ERROR(500, "CHAT_SERVICE_ERROR", "CHAT 서비스 에러");


    private final int status;
    private final String code;
    private final String message;

}

