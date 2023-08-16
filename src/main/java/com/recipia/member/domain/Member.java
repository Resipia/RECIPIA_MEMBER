package com.recipia.member.domain;

import com.recipia.member.domain.auditingfield.UpdateDateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/** 회원 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends UpdateDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;                // 회원 pk

    @Column(name = "id", nullable = false)
    private String username;          // 회원 로그인 id

    @Column(name = "password", nullable = false)
    private String password;        // 회원 비밀번호

    @Column(name = "full_name", nullable = false)
    private String fullName;        // 회원이름

    @Column(name = "nickname", nullable = false)
    private String nickname;        // 닉네임

    @Column(name = "introduction")
    private String introduction;    // 한줄소개

    @Column(name = "tel_no", nullable = false)
    private String telNo;           // 전화번호

    @Column(name = "addr1")
    private String address1;        // 주소1

    @Column(name = "addr2")
    private String address2;        // 주소2

    @Column(name = "email", nullable = false)
    private String email;           // 이메일

    @Column(name = "protection_yn", nullable = false)
    private String protectionYn;    // 개인정보 보호 동의여부

    @Column(name = "collection_yn", nullable = false)
    private String collectionYn;    // 개인정보 수집 보호여부

    @ToString.Exclude
    @OneToMany(mappedBy = "followerMember")
    private List<Follow>  followerList= new ArrayList<>();          // 팔로워 회원 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "followingMember")
    private List<Follow> followingList = new ArrayList<>();         // 팔로잉 회원 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<MemberBlock> userList = new ArrayList<>();           // 차단 신청한 회원 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "blockMember")
    private List<MemberBlock> blockList = new ArrayList<>();          // 차단시킬 회원 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<MemberFile> fileList = new ArrayList<>();            // 파일 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<Bookmark> bookmarkList = new ArrayList<>();        // 북마크 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<MemberCtgryMap> ctgryMapList = new ArrayList<>();    // 회원 카테고리 매핑 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<MemberHistoryLog> historyLogList = new ArrayList<>();// 회원 히스토리 리스트

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<MemberBadgeMap> badgeMapList = new ArrayList<>();    // 회원 뱃지 매핑 리스트


    private Member(String username, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, String email, String protectionYn, String collectionYn) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.nickname = nickname;
        this.introduction = introduction;
        this.telNo = telNo;
        this.address1 = address1;
        this.address2 = address2;
        this.email = email;
        this.protectionYn = protectionYn;
        this.collectionYn = collectionYn;
    }

    // 생성자 factory method of 선언
    public static Member of(String username, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, String email, String protectionYn, String collectionYn) {
        return new Member(username, password, fullName, nickname, introduction, telNo, address1, address2, email, protectionYn, collectionYn);
    }



}
