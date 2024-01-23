package com.recipia.member.adapter.in.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 팔로잉한 멤버의 데이터를 담을 response Dto
 */
@NoArgsConstructor
@Data
public class FollowingListResponseDto {

    private Long memberId;          // 회원 pk
    private String imageFullPath;   // 프로필 사진 저장된 경로
    private String preUrl;          // 회원 프사를 담을 url
    private String nickname;        // 회원 닉네임
    private Long followId;          // 만약 로그인한 회원이 팔로우 하고 있는 회원이면 follow pk값이 담겨있고, 내가 팔로우 하는 회원이 아니면 null 반환
    private boolean isMe;           // 이 회원이 로그인한 본인 계정이면 '나'라는걸 표시하기 위한 flag

    public FollowingListResponseDto(Long memberId, String imageFullPath, String preUrl, String nickname, Long followId, boolean isMe) {
        this.memberId = memberId;
        this.imageFullPath = imageFullPath;
        this.preUrl = preUrl;
        this.nickname = nickname;
        this.followId = followId;
        this.isMe = isMe;
    }

    public static FollowingListResponseDto of(Long memberId, String imageFullPath,String preUrl, String nickname, Long followId, boolean isMe) {
        return new FollowingListResponseDto(memberId, imageFullPath, preUrl, nickname, followId, isMe);
    }

    public static FollowingListResponseDto of(Long memberId, String preUrl, String nickname, Long followId, boolean isMe) {
        return new FollowingListResponseDto(memberId, null, preUrl, nickname, followId, isMe);
    }


}
