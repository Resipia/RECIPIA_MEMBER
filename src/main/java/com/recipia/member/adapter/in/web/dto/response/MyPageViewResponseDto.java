package com.recipia.member.adapter.in.web.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 다른 유저가 보는 마이페이지 응답 객체
 */
@Getter
@NoArgsConstructor
public class MyPageViewResponseDto {

    private Long memberId;          // 회원 pk
    private String profileImageUrl; // 프로필 이미지 url
    private String nickname;        // 닉네임
    private String introduction;    // 한줄소개
    private Long followingCount; // 팔로잉 수
    private Long followerCount;  // 팔로워 수
    private String birth;           // 생년월일
    private String gender;          // 성별
    private Long followId;          // 로그인한 회원가 팔로우 하고있는 회원이면 데이터가 들어있고 팔로우 하고있지 않은 회원이면 null

    private MyPageViewResponseDto(Long memberId, String profileImageUrl,  String nickname, String introduction, Long followingCount, Long followerCount, String birth, String gender, Long followId) {
        this.memberId = memberId;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.introduction = introduction;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.birth = birth;
        this.gender = gender;
        this.followId = followId;
    }

    public static MyPageViewResponseDto of(Long memberId, String profileImageUrl, String nickname, String introduction, Long followingCount, Long followerCount, String birth, String gender, Long followId) {
        return new MyPageViewResponseDto(memberId, profileImageUrl, nickname, introduction, followingCount, followerCount, birth, gender, followId);
    }
}
