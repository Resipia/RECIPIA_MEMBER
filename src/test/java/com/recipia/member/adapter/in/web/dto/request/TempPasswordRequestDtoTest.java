package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] 임시 비밀번호 재발급 요청 dto 테스트")
class TempPasswordRequestDtoTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("[happy] 이메일이 올바르게 제공되었을 때 성공한다.")
    @Test
    void validData() {
        //given
        TempPasswordRequestDto dto = TempPasswordRequestDto.of("fullname", "01011110000", "email@naver.com");

        //when
        Set<ConstraintViolation<TempPasswordRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();
    }

    @DisplayName("[bad] 이메일이 null일 때 에러를 반환한다.")
    @Test
    void nullEmail() {
        //given
        TempPasswordRequestDto dto = TempPasswordRequestDto.of("fullname", "01011110000", null);

        //when
        Set<ConstraintViolation<TempPasswordRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // email이 null이므로
    }

}