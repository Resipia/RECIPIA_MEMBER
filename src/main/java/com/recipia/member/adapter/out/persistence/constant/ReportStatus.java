package com.recipia.member.adapter.out.persistence.constant;

import lombok.Getter;

/**
 * 신고 상태
 */
@Getter
public enum ReportStatus {

    RECEIVED("접수"),
    COMPLETED("완료");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }

}
