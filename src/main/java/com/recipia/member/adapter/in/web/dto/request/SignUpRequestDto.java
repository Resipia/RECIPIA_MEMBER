package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
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
    private String telNo;           // 전화번호

    private String address1;        // 주소1
    private String address2;        // 주소2

    @NotBlank
    @Size(max = 1)
    private String collectionConsentYn;    // 개인정보 수집 및 이용 동의 여부

    @NotBlank
    @Size(max = 1)
    private String marketingConsentYn;    // 마케팅 활용 동의 여부

    @NotBlank
    @Size(max = 1)
    private String privacyPolicyConsentYn;    // 개인정보 보호 정책 동의 여부

    @NotBlank
    @Size(max = 1)
    private String cookieConsentYn;    // 쿠키 및 추적 기술 사용 동의 여부


    private SignUpRequestDto(String email, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, String collectionConsentYn, String marketingConsentYn, String privacyPolicyConsentYn, String cookieConsentYn) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.nickname = nickname;
        this.introduction = introduction;
        this.telNo = telNo;
        this.address1 = address1;
        this.address2 = address2;
        this.collectionConsentYn = collectionConsentYn;
        this.marketingConsentYn = marketingConsentYn;
        this.privacyPolicyConsentYn = privacyPolicyConsentYn;
        this.cookieConsentYn = cookieConsentYn;
    }

    public static SignUpRequestDto of(String email, String password, String fullName, String nickname, String introduction, String telNo, String address1, String address2, String collectionConsentYn, String marketingConsentYn, String privacyPolicyConsentYn, String cookieConsentYn) {
        return new SignUpRequestDto(email, password, fullName, nickname, introduction, telNo, address1, address2, collectionConsentYn, marketingConsentYn, privacyPolicyConsentYn, cookieConsentYn);
    }

}
