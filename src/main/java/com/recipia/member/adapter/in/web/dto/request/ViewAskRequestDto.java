package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 문의사항 조회 요청하는 request dto
 */
@ToString
@Getter
@NoArgsConstructor
public class ViewAskRequestDto {
    @NotNull
    private Long askId;

    private ViewAskRequestDto(Long askId) {
        this.askId = askId;
    }

    public static ViewAskRequestDto of(Long askId) {
        return new ViewAskRequestDto(askId);
    }
}
