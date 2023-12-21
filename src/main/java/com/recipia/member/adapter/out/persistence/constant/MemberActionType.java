package com.recipia.member.adapter.out.persistence.constant;

/**
 * 사용자 로그 추적에 사용될 action 정의 ENUM 객체
 */
public enum MemberActionType {

    // 사용자 계정 관리
    SIGN_UP("회원 가입"),
    LOGIN("로그인"),
    LOGOUT("로그아웃"),
    CHANGE_PASSWORD("비밀번호 변경"),
    EDIT_ACCOUNT("계정 정보 수정"),
    RECOVER_ACCOUNT("계정 복구"),
    DELETE_ACCOUNT("탈퇴 처리"),

    // 인증 및 권한 관리
    LOGIN_SUCCESS("로그인 성공"),
    LOGIN_FAIL("로그인 실패"),
    TOKEN_ISSUED("토큰 발급"),
    TOKEN_EXPIRED("토큰 만료"),
    PERMISSION_CHANGED("권한 변경"),

    // 보안 관련 이벤트
    SUSPICIOUS_LOGIN_ATTEMPT("의심스러운 로그인 시도"),
    SECURITY_SETTING_CHANGED("보안 설정 변경");

    private final String description;

    MemberActionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
