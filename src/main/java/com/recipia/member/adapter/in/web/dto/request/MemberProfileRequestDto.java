package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * memberId로 프로필 사진 요청하는 dto
 */
@Data
@NoArgsConstructor
public class MemberProfileRequestDto {

    @NotNull
    private Long memberId;

    @Builder
    private MemberProfileRequestDto(Long memberId) {
        this.memberId = memberId;
    }

    public static MemberProfileRequestDto of(Long memberId) {
        return new MemberProfileRequestDto(memberId);
    }
}
