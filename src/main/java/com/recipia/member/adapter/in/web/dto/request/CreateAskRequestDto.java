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
public class CreateAskRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private CreateAskRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static CreateAskRequestDto of(String title, String content) {
        return new CreateAskRequestDto(title, content);
    }
}
