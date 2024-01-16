package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.ReportRequestDto;
import com.recipia.member.adapter.out.persistence.ReportEntity;
import com.recipia.member.adapter.out.persistence.constant.ReportStatus;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@RequiredArgsConstructor
@Component
public class ReportConverter {

    private final SecurityUtils securityUtils;

    public ReportEntity domainToEntity(Report domain) {
        return ReportEntity.of(
                domain.getReportedMemberId(),
                domain.getReportingMemberId(),
                domain.getReportType(),
                domain.getReportDescription(),
                domain.getStatus()
        );
    }

    public Report dtoToDomain(ReportRequestDto dto) {
        return Report.of(
                dto.getReportedMemberId(),
                securityUtils.getCurrentMemberId(),
                dto.getReportType(),
                dto.getReportDescription(),
                ReportStatus.RECEIVED
        );
    }
}
