package com.recipia.member.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 커스텀한 에러코드를 작성한다.
 * 각 에러에 대한 상태 코드, 숫자 코드, 그리고 내부 로깅을 위한 메시지를 포함한다.
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 1. 로그인 관련 에러
    USER_NOT_FOUND(404, 1001, "유저를 찾을 수 없습니다."),
    USER_IS_DORMANT(404, 1002, "휴면계정입니다."),
    USER_IS_DEACTIVATED(404, 1003, "탈퇴한 계정입니다."),

    // 11. JWT 관련 에러
    INVALID_JWT(401, 1101, "토큰이 유효하지 않습니다."),
    EXPIRED_REFRESH_TOKEN(401, 1102, "Refresh Token이 만료되었습니다."),
    MISSING_JWT(401, 1103, "JWT 토큰이 누락되었습니다."),
    JWT_NOT_FOUND(404, 1104, "JWT를 찾을 수 없습니다."),

    // 12. Security 관련 에러
    MEMBER_INFO_NOT_FOUND_IN_SECURITY(401, 1201, "인증된 사용자의 정보를 찾을 수 없습니다."),

    // 2. 회원가입 관련 에러
    EMAIL_ALREADY_EXISTS(400, 2001, "이미 존재하는 이메일입니다."),
    TEL_NO_ALREADY_EXISTS(400, 2002, "이미 존재하는 휴대폰 번호입니다."),
    INVALID_EMAIL_FORMAT(400, 2003, "유효하지 않은 이메일 형식입니다."),
    ACCOUNT_LOCKED(401, 2004, "계정이 잠겼습니다."),
    MEMBER_FILE_SAVE_ERROR(500, 2005, "데이터 베이스에 파일을 저장하던중 예외가 발생했습니다."),

    // 3. 회원 관리 관련 에러
    ALREADY_FOLLOWING(400, 3001, "이미 팔로우 하고 있는 회원입니다."),
    NOT_ACTIVE_USER(400, 3002, "활성화 상태인 회원이 아닙니다."),

    // 31. 문의사항 관련 에러
    ASK_NOT_FOUND(400, 3101, "문의사항을 찾을 수 없습니다."),

    // 4. S3 관련 에러
    S3_UPLOAD_ERROR(500, 4001, "AWS S3 서비스 에러"),
    S3_UPLOAD_FILE_NOT_FOUND(404, 4002, "업로드할 파일이 존재하지 않습니다."),
    INVALID_FILE_TYPE(404, 4003, "S3에 업로드 할 수 없는 파일 타입입니다."),

    // 5. DB 관련 에러
    REQUIRED_FIELD_NULL(404, 5001, "필수 컬럼값이 존재하지 않습니다."),
    FILE_NOT_FOUND(404, 5002, "파일 경로에 파일이 존재하지 않습니다."),
    FILE_DUPLICATED(404, 5003, "중복된 파일입니다."),
    DB_ERROR(500, 5004, "DB ERROR"),

    // 6. Zipkin 관련 에러
    NO_TRACE_ID_IN_MESSAGE(404, 6001, "message에 traceid가 없습니다."),

    // 8. 외부 서비스 에러,
    AWS_SNS_CLIENT(500, 8001, "SNS 발행 에러"),
    RECIPE_SERVICE_ERROR(500, 8002, "RECIPE 서비스 에러"),
    WRIGGLE_SERVICE_ERROR(500, 8003, "WRIGGLE 서비스 에러"),
    CHAT_SERVICE_ERROR(500, 8004, "CHAT 서비스 에러"),
    MAIL_SEND_FAILED(500, 8005, "메일 전송 실패"),

    // 9. 공통 에러
    IO_ERROR(404, 9001, "INPUT/OUTPUT ERROR"),
    BAD_REQUEST(400, 9002, "잘못된 요청"),
    FILTER_USERNAME_PASSWORD_AUTHENTICATION_TOKEN(404, 9003, "filter attemptAuthentication 인증 에러"),
    EVENT_NOT_FOUND(404, 9004, "이벤트 저장소에 해당 이벤트를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, 9005, "서버 내부 오류"),
    NULL_POINTER_EXCEPTION(500, 9006, "Null 참조 오류"),
    ILLEGAL_ARGUMENT_EXCEPTION(400, 9007, "부적절한 인자 오류")
    ;


    private final int status;  // HTTP 상태 코드
    private final int code;    // 내부적으로 사용할 숫자 코드
    private final String message;  // 로깅을 위한 메시지

}

