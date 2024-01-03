package com.recipia.member.adapter.in.web.dto.request;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] jwt 재발행 request dto 테스트")
class JwtRepublishRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("[happy] memberId와 refreshToken이 올바르게 제공되었을 때 성공한다.")
    @Test
    void validData() {
        //given
        JwtRepublishRequestDto dto = JwtRepublishRequestDto.of(1L, "validRefreshToken");

        //when
        Set<ConstraintViolation<JwtRepublishRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();
    }

    @DisplayName("[bad] memberId가 null일 때 에러를 반환한다.")
    @Test
    void nullMemberId() {
        //given
        JwtRepublishRequestDto dto = JwtRepublishRequestDto.of(null, "validRefreshToken");

        //when
        Set<ConstraintViolation<JwtRepublishRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // memberId가 null이므로
    }

    @DisplayName("[bad] refreshToken이 비어 있을 때 에러를 반환한다.")
    @Test
    void emptyRefreshToken() {
        //given
        JwtRepublishRequestDto dto = JwtRepublishRequestDto.of(1L, "");

        //when
        Set<ConstraintViolation<JwtRepublishRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // refreshToken이 비어 있으므로
    }

    @DisplayName("[bad] refreshToken이 null일 때 에러를 반환한다.")
    @Test
    void nullRefreshToken() {
        //given
        JwtRepublishRequestDto dto = JwtRepublishRequestDto.of(1L, null);

        //when
        Set<ConstraintViolation<JwtRepublishRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // refreshToken이 null이므로
    }

}