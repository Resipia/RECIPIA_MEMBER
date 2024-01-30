package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문의사항 도메인 객체
 */
@Getter
@NoArgsConstructor
public class Ask {

    private Long id; // 문의 ID
    private Long memberId;
    private String title; // 문의 제목
    private String content; // 문의 내용
    private String answer; // 답변

    private Ask(Long id, Long memberId, String title, String content, String answer) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.answer = answer;
    }

    public static Ask of(Long id, Long memberId, String title, String content, String answer) {
        return new Ask(id, memberId, title, content, answer);
    }

    public static Ask of(Long memberId, String title, String content, String answer) {
        return new Ask(null, memberId, title, content, answer);
    }

    public static Ask of(Long memberId, String title, String content) {
        return new Ask(null, memberId, title, content, null);
    }
}
