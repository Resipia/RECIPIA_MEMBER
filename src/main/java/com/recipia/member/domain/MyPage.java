package com.recipia.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MyPage {

    private Long memberId;                  // 회원 pk
    private String profileImageFilePath;    // 프로필 이미지 저장 경로
    private String profileImageUrl;         // 임시로 발급된 프로필 이미지의 pre-signed-url
    private String nickname;                // 닉네임
    private String introduction;            // 한줄소개
    private Long followingCount;            // 팔로잉 수
    private Long followerCount;             // 팔로워 수

    @Builder
    public MyPage(Long memberId, String profileImageFilePath, String profileImageUrl, String nickname, String introduction, Long followingCount, Long followerCount) {
        this.memberId = memberId;
        this.profileImageFilePath = profileImageFilePath;
        this.profileImageUrl = profileImageUrl;
        this.nickname = nickname;
        this.introduction = introduction;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }

    /**
     * 마이페이지 조회 응답 test용
     */
    public static MyPage of(Long memberId, String profileImageUrl, String nickname, String introduction, Long followingCount, Long followerCount) {
        return new MyPage(memberId, null, profileImageUrl, nickname, introduction, followingCount, followerCount);
    }

    /**
     * 마이페이지 조회 요청 test용
     *
     * @param memberId 마이페이지 조회 요청 memberId
     */
    public static MyPage of(Long memberId) {
        return new MyPage(memberId, null, null, null, null, null, null);
    }

    /**
     * 마이페이지 수정 요청 test용
     */
    public static MyPage of(Long memberId, String nickname, String introduction) {
        return new MyPage(memberId, null, null, nickname, introduction, null, null);
    }
}
