package com.recipia.member.hexagonal.domain;

import com.recipia.member.hexagonal.adapter.out.persistence.constant.MemberStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member {

    private Long id;                // 회원 pk
    private String username;          // 회원 로그인 id
    private String password;        // 회원 비밀번호
    private String fullName;        // 회원이름
    private String nickname;        // 닉네임
    private MemberStatus status;        // 회원상태
    private String introduction;    // 한줄소개
    private String telNo;           // 전화번호
    private String address1;        // 주소1
    private String address2;        // 주소2
    private String email;           // 이메일
    private String protectionYn;    // 개인정보 보호 동의여부
    private String collectionYn;    // 개인정보 수집 보호여부

    private Member(Long id, String username, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, String email, String protectionYn, String collectionYn) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.nickname = nickname;
        this.status = status;
        this.introduction = introduction;
        this.telNo = telNo;
        this.address1 = address1;
        this.address2 = address2;
        this.email = email;
        this.protectionYn = protectionYn;
        this.collectionYn = collectionYn;
    }

    public static Member of(Long id, String username, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, String email, String protectionYn, String collectionYn) {
        return new Member(id, username, password, fullName, nickname, status, introduction, telNo, address1, address2, email, protectionYn, collectionYn);
    }


}

