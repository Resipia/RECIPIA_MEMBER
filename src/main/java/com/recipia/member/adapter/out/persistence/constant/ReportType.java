package com.recipia.member.adapter.out.persistence.constant;

import lombok.Getter;

/**
 * 회원 신고 유형
 */
@Getter
public enum ReportType {
    INAPPROPRIATE_CONTENT("부적절한 내용 (욕설, 폭력적 또는 성적인 내용 등)"),
    SPAM("스팸 또는 광고"),
    COPYRIGHT_INFRINGEMENT("저작권 침해"),
    OTHER("기타");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

}
