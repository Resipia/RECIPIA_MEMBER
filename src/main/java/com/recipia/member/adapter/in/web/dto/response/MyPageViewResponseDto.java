package com.recipia.member.adapter.in.web.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageViewResponseDto {

    private Long memberId;          // 회원 pk
    private String profileImageUrl; // 프로필 이미지 url
    private String nickname;        // 닉네임
    private String introduction;    // 한줄소개
    private Long followingCount; // 팔로잉 수
    private Long followerCount;  // 팔로워 수

    private MyPageViewResponseDto(Long memberId, String profileImageUrl,  String nickname, String introduction, Long followingCount, Long followerCount) {
        this.memberId = memberId;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.introduction = introduction;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }

    public static MyPageViewResponseDto of(Long memberId, String profileImageUrl, String nickname, String introduction, Long followingCount, Long followerCount) {
        return new MyPageViewResponseDto(memberId, profileImageUrl, nickname, introduction, followingCount, followerCount);
    }
}
