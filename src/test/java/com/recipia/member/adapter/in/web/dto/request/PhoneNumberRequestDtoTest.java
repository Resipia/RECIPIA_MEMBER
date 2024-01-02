package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class PhoneNumberRequestDtoTest {


    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("[happy] 올바른 전화번호 입력 시 성공한다.")
    @Test
    void validPhoneNumber() {
        //given
        PhoneNumberRequestDto dto = PhoneNumberRequestDto.of("01012345678");

        //when
        Set<ConstraintViolation<PhoneNumberRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();
    }

    @DisplayName("[bad] 전화번호가 비어있을 때 에러를 반환한다.")
    @Test
    void invalidPhoneNumber() {
        //given
        PhoneNumberRequestDto dto = PhoneNumberRequestDto.of("");

        //when
        Set<ConstraintViolation<PhoneNumberRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // 전화번호가 비어있으므로
    }


}