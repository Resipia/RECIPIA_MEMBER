package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 회원가입을 요청할때 데이터를 담는 request dto
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    @Size(max = 50)
    private String email;           // 이메일

    @NotBlank
    @Size(max = 20)
    private String password;        // 회원 비밀번호

    @NotBlank
    @Size(max = 20)
    private String fullName;        // 회원이름

    @NotBlank
    @Size(max = 20)
    private String nickname;        // 닉네임
    private String introduction;    // 한줄소개

    @NotBlank
    @Size(max = 25)
    @Pattern(regexp = "\\d+")       // 문자열 안에서 숫자만 입력 가능
    private String telNo;           // 전화번호

    private String address1;        // 주소1
    private String address2;        // 주소2

    private MultipartFile profileImage; // 프로필 이미지

//    @NotBlank
//    private String isPersonalInfoConsent;    // 개인정보 수집 및 이용 동의
//    @NotBlank
//    private String isDataRetentionConsent;    // 개인정보 보관 및 파기 동의

    @Builder
    private SignUpRequestDto(String email, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, MultipartFile profileImage) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.nickname = nickname;
        this.introduction = introduction;
        this.telNo = telNo;
        this.address1 = address1;
        this.address2 = address2;
        this.profileImage = profileImage;
    }

    public static SignUpRequestDto of(String email, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, MultipartFile profileImage) {
        return new SignUpRequestDto(email, password, fullName, nickname, introduction, telNo, address1, address2, profileImage);
    }

    public static SignUpRequestDto of(String email, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2) {
        return new SignUpRequestDto(email, password, fullName, nickname, introduction, telNo, address1, address2, null);
    }

    public static SignUpRequestDto of(String email, String password, String nickname, String introduction, MultipartFile profileImage) {
        return new SignUpRequestDto(email, password, null, nickname, introduction, null, null, null, profileImage);
    }

    public static SignUpRequestDto of(String email, String password, String nickname, String introduction) {
        return new SignUpRequestDto(email, password, null, nickname, introduction, null, null, null, null);
    }

}
