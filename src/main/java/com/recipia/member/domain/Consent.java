package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 동의 도메인 객체
 */
@Getter
@NoArgsConstructor
public class Consent {

    private Long id;                        // 동의 pk
    private Member member;
    private String personalInfoConsent;     // 개인정보 수집 및 이용 동의
    private String dataRetentionConsent;     // 개인정보 보관 및 파기 동의

    private Consent(Long id, Member member, String personalInfoConsent, String dataRetentionConsent) {
        this.id = id;
        this.member = member;
        this.personalInfoConsent = personalInfoConsent;
        this.dataRetentionConsent = dataRetentionConsent;
    }

    public static Consent of(Long id, Member member, String personalInfoConsent, String dataRetentionConsent) {
        return new Consent(id, member, personalInfoConsent, dataRetentionConsent);
    }

    public static Consent of(Member member, String personalInfoConsent, String dataRetentionConsent) {
        return new Consent(null, member, personalInfoConsent, dataRetentionConsent);
    }
}
