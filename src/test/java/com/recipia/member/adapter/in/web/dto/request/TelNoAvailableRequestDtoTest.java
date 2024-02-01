package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[단위] 전화번호 중복체크 요청 dto 테스트")
class TelNoAvailableRequestDtoTest {

    // Java의 Bean Validation API를 사용해 유효성 검증을 수행하기 위한 Validator 객체를 생성
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("[happy] 필수값이 전부 들어왔을때 성공한다.")
    @Test
    void requestValidateSuccess() {
        //given
        TelNoAvailableRequestDto dto = TelNoAvailableRequestDto.of("01011111111");

        //when
        // ConstraintViolation은 유효성 검사에서 발견된 제약 조건 위반을 나타내는 클래스다.
        Set<ConstraintViolation<TelNoAvailableRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();

    }

    @DisplayName("[happy] telNo에 숫자 이외의 다른 문자가 들어왔을때 실패한다.")
    @Test
    void requestValidateFail() {
        //given
        TelNoAvailableRequestDto dto = TelNoAvailableRequestDto.of("010-1111-1111");

        //when
        // ConstraintViolation은 유효성 검사에서 발견된 제약 조건 위반을 나타내는 클래스다.
        Set<ConstraintViolation<TelNoAvailableRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).hasSize(1);

    }
}