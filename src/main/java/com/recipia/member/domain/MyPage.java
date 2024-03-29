package com.recipia.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 마이페이지에 관련된 도메인
 */
@Setter
@Getter
@NoArgsConstructor
public class MyPage {

    private Long memberId;                  // 회원 pk
    private String imageFilePath;           // 프로필 이미지 저장 경로
    private String imagePreUrl;         // 임시로 발급된 프로필 이미지의 pre-signed-url
    private String nickname;                // 닉네임
    private String introduction;            // 한줄소개
    private Long followingCount;            // 팔로잉 수
    private Long followerCount;             // 팔로워 수
    private String birth;                   // 생년월일
    private String gender;                  // 성별
    private Long followId;                // 로그인한 회원가 팔로우 하고있는 회원이면 데이터가 들어있고 팔로우 하고있지 않은 회원이면 null

    @Builder
    public MyPage(Long memberId, String imageFilePath, String imagePreUrl, String nickname, String introduction, Long followingCount, Long followerCount, String birth, String gender, Long followId) {
        this.memberId = memberId;
        this.imageFilePath = imageFilePath;
        this.imagePreUrl = imagePreUrl;
        this.nickname = nickname;
        this.introduction = introduction;
        this.followingCount = followingCount;
        this.followerCount = followerCount;
        this.birth = birth;
        this.gender = gender;
        this.followId = followId;
    }

    /**
     * 마이페이지 조회 응답 test용
     */
    public static MyPage of(Long memberId, String profileImageUrl, String nickname, String introduction, Long followingCount, Long followerCount, String birth, String gender) {
        return new MyPage(memberId, null, profileImageUrl, nickname, introduction, followingCount, followerCount, birth, gender, null);
    }

    /**
     * 마이페이지 조회 요청 test용
     *
     * @param memberId 마이페이지 조회 요청 memberId
     */
    public static MyPage of(Long memberId) {
        return new MyPage(memberId, null, null, null, null, null, null, null, null, null);
    }

    /**
     * 마이페이지 수정 요청 test용 - 삭제 이미지 X
     */
    public static MyPage of(Long memberId, String nickname, String introduction,  String birth, String gender) {
        return new MyPage(memberId, null, null, nickname, introduction, null, null, birth, gender, null);
    }

    /**
     * 마이페이지 수정 요청 test용 - 삭제 이미지 O
     */
    public static MyPage of(Long memberId, String nickname, String introduction) {
        return new MyPage(memberId, null, null, nickname, introduction, null, null, null, null, null);
    }

    /**
     * 다른 회원의 마이페이지 조회 응답 test용
     */
    public static MyPage of(Long memberId, String profileImageUrl, String nickname, String introduction, Long followingCount, Long followerCount, String birth, String gender, Long followId) {
        return new MyPage(memberId, null, profileImageUrl, nickname, introduction, followingCount, followerCount, birth, gender, followId);
    }

}
