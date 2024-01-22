package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
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

    @Size(max = 300)
    private String introduction;    // 한줄소개
    private MultipartFile profileImage; // 프로필 이미지
    private Integer deleteFileOrder;       // 삭제할 file order

    private String birth;
    private String gender;

    @Builder
    private UpdateMyPageRequestDto(String nickname, String introduction, MultipartFile profileImage, Integer deleteFileOrder, String birth, String gender) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileImage = profileImage;
        this.deleteFileOrder = deleteFileOrder;
        this.birth = birth;
        this.gender = gender;
    }

    public static UpdateMyPageRequestDto of(String nickname, String introduction, MultipartFile profileImage, Integer deleteFileOrder, String birth, String gender) {
        return new UpdateMyPageRequestDto(nickname, introduction, profileImage, deleteFileOrder, birth, gender);
    }

    public static UpdateMyPageRequestDto of(String nickname, String introduction, Integer deleteFileOrder, String birth, String gender) {
        return new UpdateMyPageRequestDto(nickname, introduction, null, deleteFileOrder, birth, gender);
    }

    public static UpdateMyPageRequestDto of(String nickname, String introduction, String birth, String gender) {
        return new UpdateMyPageRequestDto(nickname, introduction, null, null, null, null);
    }
}
