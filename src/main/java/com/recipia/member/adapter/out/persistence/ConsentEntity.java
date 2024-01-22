package com.recipia.member.adapter.out.persistence;

import com.recipia.member.adapter.out.persistence.auditingfield.CreateDateTimeForEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/** 회원 동의 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "consent")
public class ConsentEntity extends CreateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consent_id", nullable = false)
    private Long id;        // 동의 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity memberEntity;

    @Column(name = "personal_info_consent", nullable = false)
    private String personalInfoConsent;     // 개인정보 수집 및 이용 동의

    @Column(name = "data_retention_consent", nullable = false)
    private String dataRetentionConsent;     // 개인정보 보관 및 파기 동의

    private ConsentEntity(Long id, MemberEntity memberEntity, String personalInfoConsent, String dataRetentionConsent) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.personalInfoConsent = personalInfoConsent;
        this.dataRetentionConsent = dataRetentionConsent;
    }

    public static ConsentEntity of(Long id, MemberEntity memberEntity, String personalInfoConsent, String dataRetentionConsent) {
        return new ConsentEntity(id, memberEntity, personalInfoConsent, dataRetentionConsent);
    }

    public static ConsentEntity of(MemberEntity memberEntity, String personalInfoConsent, String dataRetentionConsent) {
        return new ConsentEntity(null, memberEntity, personalInfoConsent, dataRetentionConsent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsentEntity that)) return false;
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
