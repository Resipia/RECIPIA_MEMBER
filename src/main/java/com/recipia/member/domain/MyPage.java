package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPage {

    private Long memberId;          // 회원 pk
    private String nickname;        // 닉네임
    private String introduction;    // 한줄소개
    private Long followingCount; // 팔로잉 수
    private Long followerCount;  // 팔로워 수

    public MyPage(Long memberId, String nickname, String introduction, Long followingCount, Long followerCount) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.introduction = introduction;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }

    public static MyPage of(Long memberId, String nickname, String introduction, Long followingCount, Long followerCount) {
        return new MyPage(memberId, nickname, introduction, followingCount, followerCount);
    }

    public static MyPage of(Long memberId) {
        return new MyPage(memberId, null, null, null, null);
    }
}
