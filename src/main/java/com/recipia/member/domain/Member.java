package com.recipia.member.domain;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 멤버 도메인 객체
 */
@Setter
@Getter
@NoArgsConstructor
public class Member {

    private Long id;                // 회원 pk
    private String email;           // 이메일
    private String password;        // 회원 비밀번호
    private String fullName;        // 회원이름
    private String nickname;        // 닉네임
    private MemberStatus status;    // 회원상태
    private String introduction;    // 한줄소개
    private String telNo;           // 전화번호
    private String address1;        // 주소1
    private String address2;        // 주소2
    private RoleType roleType;      // 계정 권한
    private MemberFile profileImage; // 프로필 이미지
    private String personalInfoConsent;     // 개인정보 수집 및 이용 동의
    private String dataRetentionConsent;     // 개인정보 보관 및 파기 동의

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    @Builder
    private Member(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType, MemberFile profileImage, String isPersonalInfoConsent, String isDataRetentionConsent) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.nickname = nickname;
        this.status = status;
        this.introduction = introduction;
        this.telNo = telNo;
        this.address1 = address1;
        this.address2 = address2;
        this.roleType = roleType;
        this.profileImage = profileImage;
        this.personalInfoConsent = isPersonalInfoConsent;
        this.dataRetentionConsent = isDataRetentionConsent;
    }

    /**
     * 파일 이미지 없을때 생성하는 컨버터
     */
    public static Member of(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType, String isPersonalInfoConsent, String isDataRetentionConsent) {
        return new Member(id, email, password, fullName, nickname, status, introduction, telNo, address1, address2, roleType, null, isPersonalInfoConsent, isDataRetentionConsent);
    }

    public static Member of(String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType, String isPersonalInfoConsent, String isDataRetentionConsent) {
        return new Member(null, email, password, fullName, nickname, status, introduction, telNo, address1, address2, roleType, null, isPersonalInfoConsent, isDataRetentionConsent);
    }

    /**
     * 파일 이미지 있을때 생성하는 컨버터
     */
    public static Member of(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType, MemberFile profileImage, String isPersonalInfoConsent, String isDataRetentionConsent) {
        return new Member(id, email, password, fullName, nickname, status, introduction, telNo, address1, address2, roleType, profileImage, isPersonalInfoConsent, isDataRetentionConsent);
    }

    public static Member of(Long id) {
        return new Member(id, null, null, null, null, null, null, null, null, null, null, null, "Y", "Y");
    }

    /**
     * 비밀번호가 영어 대,소문자, 숫자, 특수문자가 하나씩 무조건 포함되어야하고 8글자 이상 20자 이하인지 검증하는 메소드
     * @param password
     * @return
     */
    public boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else hasSpecial = true;

            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                return true;
            }
        }

        return false;
    }

    /**
     * 암호화된 비밀번호로 업데이트해주는 메서드
     */
    public void updatePwToEncoded(String encodedPassword) {
        this.password = encodedPassword;
    }
}

