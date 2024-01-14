package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * 마이페이지 수정 요청을 담당하는 request dto
 */
@ToString
@Getter
@NoArgsConstructor
public class UpdateMyPageRequestDto {

    @NotBlank
    private String nickname;        // 닉네임
    private String introduction;    // 한줄소개
    private MultipartFile profileImage; // 프로필 이미지

    private UpdateMyPageRequestDto(String nickname, String introduction, MultipartFile profileImage) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileImage = profileImage;
    }

    public static UpdateMyPageRequestDto of(String nickname, String introduction, MultipartFile profileImage) {
        return new UpdateMyPageRequestDto(nickname, introduction, profileImage);
    }

    public static UpdateMyPageRequestDto of(String nickname, String introduction) {
        return new UpdateMyPageRequestDto(nickname, introduction, null);
    }
}
