package com.recipia.member.hexagonal.adapter.out.persistence.auditingfield;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * Auditing 필드 - 생성시간 + 수정시간 등록
 * 이 필드를 상속받으면 생성시간과 수정시간 모두 auditing이 적용된다.
 */
@ToString
@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class UpdateDateTimeForEntity extends CreateDateTimeForEntity {

    // 수정시간
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(name = "update_dttm", nullable = false)
    private LocalDateTime updateDateTime;

}
