package com.recipia.member.adapter.out.persistence;

import com.recipia.member.adapter.out.persistence.auditingfield.UpdateDateTimeForEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/** 회원 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class MemberEntity extends UpdateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;                // 회원 pk

    @Column(name = "email", nullable = false)
    private String email;           // 이메일

    @Column(name = "password", nullable = false)
    private String password;        // 회원 비밀번호

    @Column(name = "full_name", nullable = false)
    private String fullName;        // 회원이름

    @Column(name = "nickname", nullable = false)
    private String nickname;        // 닉네임

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus status;        // 회원상태

    @Column(name = "introduction")
    private String introduction;    // 한줄소개

    @Column(name = "tel_no", nullable = false)
    private String telNo;           // 전화번호

    @Column(name = "addr1")
    private String address1;        // 주소1

    @Column(name = "addr2")
    private String address2;        // 주소2

    @Column(name = "protection_yn", nullable = false)
    private String protectionYn;    // 개인정보 보호 동의여부

    @Column(name = "collection_yn", nullable = false)
    private String collectionYn;    // 개인정보 수집 보호여부

    @Column(name = "role_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;        // 회원상태


    // private 생성자
    private MemberEntity(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, String protectionYn, String collectionYn, RoleType roleType) {
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
        this.protectionYn = protectionYn;
        this.collectionYn = collectionYn;
        this.roleType = roleType;
    }

    // 새 엔티티 생성용 팩토리 메소드
    public static MemberEntity of(String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, String protectionYn, String collectionYn, RoleType roleType) {
        return new MemberEntity(null, email, password, fullName, nickname, status, introduction, telNo, address1, address2, protectionYn, collectionYn, roleType);
    }

    // 기존 엔티티 로드용 팩토리 메소드
    public static MemberEntity of(Long id, String email, String password, String fullName, String nickname, MemberStatus status, String introduction, String telNo, String address1, String address2, String protectionYn, String collectionYn, RoleType roleType) {
        return new MemberEntity(id, email, password, fullName, nickname, status, introduction, telNo, address1, address2, protectionYn, collectionYn, roleType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberEntity that)) return false;
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    // update용 메소드
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
