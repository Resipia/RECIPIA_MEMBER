package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] 휴대폰 번호 문자 전송 요청 dto 테스트")
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
        assertThat(violations).hasSize(2); // 전화번호가 비어있고, 숫자로 이루어진게 아니므로 2개
    }

    @DisplayName("[bad] 전화번호에 숫자 말고 문자가 들어왔을때 에러를 반환한다.")
    @Test
    void invalidPhoneNumberWithString() {
        //given
        PhoneNumberRequestDto dto = PhoneNumberRequestDto.of("onethothree");

        //when
        Set<ConstraintViolation<PhoneNumberRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // 전화번호가 숫자가 아니라 문자가 들어왔기 때문
    }


}