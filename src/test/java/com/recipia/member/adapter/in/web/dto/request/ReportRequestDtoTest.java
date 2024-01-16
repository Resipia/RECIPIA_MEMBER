package com.recipia.member.adapter.in.web.dto.request;

import com.recipia.member.adapter.out.persistence.constant.ReportType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] 회원 신고 접수 요청 dto 테스트")
class ReportRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("[happy] reportedMemberId와 reportType이 올바르게 제공되었을 때 성공한다.")
    @Test
    void validRequest() {
        //given
        ReportRequestDto dto = ReportRequestDto.of(3L, ReportType.SPAM, "");

        //when
        Set<ConstraintViolation<ReportRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();
    }

    @DisplayName("[bad] reportedMemberId가 비어있을 때 에러를 반환한다.")
    @Test
    void invalidRequest() {
        //given
        ReportRequestDto dto = ReportRequestDto.of(null, ReportType.SPAM, "");

        //when
        Set<ConstraintViolation<ReportRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // reportedMemberId가 비어있기 때문에 1
    }


}