package com.recipia.member.adapter.in.web;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    private String email;           // 이메일
    private String password;        // 회원 비밀번호
    private String fullName;        // 회원이름
    private String nickname;        // 닉네임
    private String introduction;    // 한줄소개
    private String telNo;           // 전화번호
    private String address1;        // 주소1
    private String address2;        // 주소2
    private String protectionYn;    // 개인정보 보호 동의여부
    private String collectionYn;    // 개인정보 수집 보호여부

    private SignUpRequestDto(String email, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, String protectionYn, String collectionYn) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.nickname = nickname;
        this.introduction = introduction;
        this.telNo = telNo;
        this.address1 = address1;
        this.address2 = address2;
        this.protectionYn = protectionYn;
        this.collectionYn = collectionYn;
    }

    public static SignUpRequestDto of(String email, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, String protectionYn, String collectionYn) {
        return new SignUpRequestDto(email, password, fullName, nickname, introduction, telNo, address1, address2, protectionYn, collectionYn);
    }
}
