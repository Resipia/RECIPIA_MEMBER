package com.recipia.member.adapter.in.web.dto.request;

import com.recipia.member.adapter.out.persistence.constant.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 회원 신고를 요청할때 데이터를 담는 request dto
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class ReportRequestDto {

    @NotNull
    private Long reportedMemberId;      // 신고당한 회원의 id
    @NotNull
    private ReportType reportType;      // 신고 유형
    private String reportDescription;   // 신고 설명

    private ReportRequestDto(Long reportedMemberId, ReportType reportType, String reportDescription) {
        this.reportedMemberId = reportedMemberId;
        this.reportType = reportType;
        this.reportDescription = reportDescription;
    }

    public static ReportRequestDto of(Long reportedMemberId, ReportType reportType, String reportDescription) {
        return new ReportRequestDto(reportedMemberId, reportType, reportDescription);
    }
}
