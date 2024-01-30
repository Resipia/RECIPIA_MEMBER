package com.recipia.member.adapter.in.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 문의사항 목록을 담을 response Dto
 */
@NoArgsConstructor
@Data
public class AskListResponseDto {
    private Long id;
    private String title;
    private boolean answerYn;
    private String createDate;

    public AskListResponseDto(Long id, String title, boolean answerYn, String createDate) {
        this.id = id;
        this.title = title;
        this.answerYn = answerYn;
        this.createDate = createDate;
    }

    public static AskListResponseDto of(Long id, String title, boolean answerYn, String createDate) {
        return new AskListResponseDto(id, title, answerYn, createDate);
    }

}
