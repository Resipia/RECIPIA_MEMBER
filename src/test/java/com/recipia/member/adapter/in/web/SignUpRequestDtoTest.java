package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.SignUpRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SignUpRequestDtoTest {

    // Java의 Bean Validation API를 사용해 유효성 검증을 수행하기 위한 Validator 객체를 생성
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @DisplayName("[happy] 필수값이 전부 들어왔을때 성공한다.")
    @Test
    void requestValidateSuccess() {
        //given
        SignUpRequestDto dto = SignUpRequestDto.of(
                "test@example.com", "password123", "John Doe", "johnny", "Hello, I'm John",
                "1234567890", "123 Street", "Apt 101", "Y", "Y", "Y", "Y"
        );

        //when
        // ConstraintViolation은 유효성 검사에서 발견된 제약 조건 위반을 나타내는 클래스다.
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();

    }


    @DisplayName("[bad] 필수값이 누락되었을때 에러를 반환한다.")
    @Test
    void requestValidateFail() {
        //given
        SignUpRequestDto dto = SignUpRequestDto.of(
                "", "", "John Doe", "johnny", "Hello, I'm John",
                "1234567890", "123 Street", "Apt 101", "Y", "Y", "Y", "Y"
        );

        //when
        // ConstraintViolation은 유효성 검사에서 발견된 제약 조건 위반을 나타내는 클래스다.
        Set<ConstraintViolation<SignUpRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(2); // 두 필드 모두 유효성 검사에 실패했으므로

    }


}