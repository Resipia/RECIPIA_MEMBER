package com.recipia.member.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀한 에러코드를 작성한다.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // todo: 숫자로 정의한 errorcode 추가하고 클라이언트한테 반환할때는 status, errorcode만 추가
    // todo: log.error에 찍을때는 우리가 알아보기 편하게 문자 에러메세지는 추가
    // 로그인 관련 에러
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "유저를 찾을 수 없습니다."),
    USER_IS_DORMANT(404, "USER_IS_DORMANT", "휴면계정입니다."),
    USER_IS_DEACTIVATED(404, "USER_IS_DEACTIVATED", "탈퇴한 계정입니다."),

    // JWT 관련 에러
    INVALID_JWT(401, "INVALID_JWT", "토큰이 유효하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(401, "EXPIRED_REFRESH_TOKEN", "Refresh Token이 만료되었습니다."),
    MISSING_JWT(401, "MISSING_JWT", "JWT 토큰이 누락되었습니다."),
    JWT_NOT_FOUND(404, "JWT_NOT_FOUND", "JWT를 찾을 수 없습니다."),

    // 회원가입 관련 에러
    EMAIL_ALREADY_EXISTS(400, "EMAIL_ALREADY_EXISTS", "이미 존재하는 이메일입니다."),
    TEL_NO_ALREADY_EXISTS(400, "TEL_NO_ALREADY_EXISTS", "이미 존재하는 휴대폰 번호입니다."),
    INVALID_EMAIL_FORMAT(400, "INVALID_EMAIL_FORMAT", "유효하지 않은 이메일 형식입니다."),
    ACCOUNT_LOCKED(401, "ACCOUNT_LOCKED", "계정이 잠겼습니다."),
    MEMBER_FILE_SAVE_ERROR(500, "MEMBER_FILE_SAVE_ERROR", "데이터 베이스에 파일을 저장하던중 예외가 발생했습니다."),

    // Security 관련 에러
    MEMBER_INFO_NOT_FOUND_IN_SECURITY(404, "MEMBER_INFO_NOT_FOUND_IN_SECURITY", "인증된 사용자의 정보를 찾을 수 없습니다."),


    // 회원 관리 관련 에러
    ALREADY_FOLLOWING(400, "ALREADY_FOLLOWING", "이미 팔로우 하고 있는 회원입니다."),

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
    CHAT_SERVICE_ERROR(500, "CHAT_SERVICE_ERROR", "CHAT 서비스 에러"),
    S3_UPLOAD_ERROR(500, "S3_UPLOAD_ERROR", "AWS S3 서비스 에러")
    ;


    private final int status;
    private final String code;
    private final String message;

}

