package com.recipia.member.domain;

import com.recipia.member.adapter.out.persistence.constant.ReportStatus;
import com.recipia.member.adapter.out.persistence.constant.ReportType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 신고 도메인 객체
 */
@NoArgsConstructor
@Getter
@Setter
public class Report {

    private Long id;
    private Long reportedMemberId;      // 신고당한 회원의 id
    private Long reportingMemberId;     // 신고를 한 회원의 id
    private ReportType reportType;      // 신고 유형
    private String reportDescription;   // 신고 설명
    private ReportStatus status;        // 신고 상태 (접수, 완료)
    private String adminOpinion;        // 관리자 의견

    private Report(Long id, Long reportedMemberId, Long reportingMemberId, ReportType reportType, String reportDescription, ReportStatus status, String adminOpinion) {
        this.id = id;
        this.reportedMemberId = reportedMemberId;
        this.reportingMemberId = reportingMemberId;
        this.reportType = reportType;
        this.reportDescription = reportDescription;
        this.status = status;
        this.adminOpinion = adminOpinion;
    }

    public static Report of(Long id, Long reportedMemberId, Long reportingMemberId, ReportType reportType, String reportDescription, ReportStatus status, String adminOpinion) {
        return new Report(id, reportedMemberId, reportingMemberId, reportType, reportDescription, status, adminOpinion);
    }

    /**
     * 신고 접수용
     */
    public static Report of(Long reportedMemberId, Long reportingMemberId, ReportType reportType, String reportDescription, ReportStatus status) {
        return new Report(null, reportedMemberId, reportingMemberId, reportType, reportDescription, status, null);
    }
}
