package com.recipia.member.adapter.out.persistence;


import com.recipia.member.adapter.out.persistence.auditingfield.UpdateDateTimeForEntity;
import com.recipia.member.adapter.out.persistence.constant.ReportStatus;
import com.recipia.member.adapter.out.persistence.constant.ReportType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 회원 신고 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "report")
public class ReportEntity extends UpdateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "reported_member_id", nullable = false)
    private Long reportedMemberId;      // 신고당한 회원의 id

    @Column(name = "reporting_member_id", nullable = false)
    private Long reportingMemberId;     // 신고를 한 회원의 id

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 50)
    private ReportType reportType;      // 신고 유형

    @Column(name = "report_description", length = 200)
    private String reportDescription;   // 신고 설명

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ReportStatus status;        // 신고 상태 (접수, 완료)

    @Column(name = "admin_opinion", length = 200)
    private String adminOpinion;        // 관리자 의견

    private ReportEntity(Long id, Long reportedMemberId, Long reportingMemberId, ReportType reportType, String reportDescription, ReportStatus status, String adminOpinion) {
        this.id = id;
        this.reportedMemberId = reportedMemberId;
        this.reportingMemberId = reportingMemberId;
        this.reportType = reportType;
        this.reportDescription = reportDescription;
        this.status = status;
        this.adminOpinion = adminOpinion;
    }

    public static ReportEntity of(Long id, Long reportedMemberId, Long reportingMemberId, ReportType reportType, String reportDescription, ReportStatus status, String adminOpinion) {
        return new ReportEntity(id, reportedMemberId, reportingMemberId, reportType, reportDescription, status, adminOpinion);
    }

    public static ReportEntity of(Long reportedMemberId, Long reportingMemberId, ReportType reportType, String reportDescription, ReportStatus status, String adminOpinion) {
        return new ReportEntity(null, reportedMemberId, reportingMemberId, reportType, reportDescription, status, adminOpinion);
    }

    public static ReportEntity of(Long reportedMemberId, Long reportingMemberId, ReportType reportType, String reportDescription, ReportStatus status) {
        return new ReportEntity(null, reportedMemberId, reportingMemberId, reportType, reportDescription, status, null);
    }
}
