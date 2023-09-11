package com.recipia.member.dto;

import com.recipia.member.domain.Member;
import com.recipia.member.domain.constant.MemberStatus;

import java.time.LocalDateTime;


public record MemberDto(
        Long id,
        String username,
        String password,
        String fullName,
        String nickname,
        String introduction,
        String telNo,
        String address1,
        String address2,
        String email,
        String protectionYn,
        String collectionYn,
        MemberStatus status,
        LocalDateTime createDateTime,
        LocalDateTime updateDateTime
) {

    /** 전체 생성자 factory method 선언 */
    public static MemberDto of(Long id, String username, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, String email, String protectionYn, String collectionYn, MemberStatus status, LocalDateTime createDateTime, LocalDateTime updateDateTime) {
        return new MemberDto(id, username, password, fullName, nickname, introduction, telNo, address1, address2, email, protectionYn, collectionYn, status, createDateTime, updateDateTime);
    }

    /** entity를 dto로 변환하는 factory method 선언 */
    public static MemberDto fromEntity(Member entity) {
        return of(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getFullName(),
                entity.getNickname(),
                entity.getIntroduction(),
                entity.getTelNo(),
                entity.getAddress1(),
                entity.getAddress2(),
                entity.getEmail(),
                entity.getProtectionYn(),
                entity.getCollectionYn(),
                entity.getStatus(),
                entity.getCreateDateTime(),
                entity.getUpdateDateTime()
        );
    }


}