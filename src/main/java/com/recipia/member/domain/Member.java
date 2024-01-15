package com.recipia.member.domain;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private Member(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType, MemberFile profileImage) {
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
    }

    /**
     * 파일 이미지 없을때 생성하는 컨버터
     */
    public static Member of(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType) {
        return new Member(id, email, password, fullName, nickname, status, introduction, telNo, address1, address2, roleType, null);
    }

    public static Member of(String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType) {
        return new Member(null, email, password, fullName, nickname, status, introduction, telNo, address1, address2, roleType, null);
    }

    /**
     * 파일 이미지 있을때 생성하는 컨버터
     */
    public static Member of(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, RoleType roleType, MemberFile profileImage) {
        return new Member(id, email, password, fullName, nickname, status, introduction, telNo, address1, address2, roleType, profileImage);
    }

    public static Member of(Long id) {
        return new Member(id, null, null, null, null, null, null, null, null, null, null, null);
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
     * 비밀번호를 암호화하는 인스턴스 메소드
     */
    public void passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(this.password);
    }


}

