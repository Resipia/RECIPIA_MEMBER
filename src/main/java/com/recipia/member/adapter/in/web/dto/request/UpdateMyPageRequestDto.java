package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 마이페이지 수정 요청을 담당하는 request dto
 */
@ToString
@Data
@NoArgsConstructor
public class UpdateMyPageRequestDto {

    @NotBlank
    private String nickname;        // 닉네임

    @Size(max = 300)
    private String introduction;    // 한줄소개
    private MultipartFile profileImage; // 프로필 이미지

    private String birth;
    private String gender;

    @Builder
    private UpdateMyPageRequestDto(String nickname, String introduction, MultipartFile profileImage, String birth, String gender) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileImage = profileImage;
        this.birth = birth;
        this.gender = gender;
    }

    public static UpdateMyPageRequestDto of(String nickname, String introduction, MultipartFile profileImage,  String birth, String gender) {
        return new UpdateMyPageRequestDto(nickname, introduction, profileImage, birth, gender);
    }

    public static UpdateMyPageRequestDto of(String nickname, String introduction, String birth, String gender) {
        return new UpdateMyPageRequestDto(nickname, introduction, null, birth, gender);
    }

}
