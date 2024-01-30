package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 문의사항 등록 요청하는 request dto
 */
@ToString
@Getter
@NoArgsConstructor
public class AskRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private AskRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static AskRequestDto of(String title, String content) {
        return new AskRequestDto(title, content);
    }
}
