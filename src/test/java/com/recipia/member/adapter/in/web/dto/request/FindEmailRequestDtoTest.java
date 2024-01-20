package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] 이메일 찾기 요청 request dto 테스트")
class FindEmailRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("[happy] 이름, 전화번호가 올바르게 제공되었을 때 성공한다.")
    @Test
    void validData() {
        //given
        FindEmailRequestDto dto = FindEmailRequestDto.of("fullName", "01000001111");

        //when
        Set<ConstraintViolation<FindEmailRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();
    }

    @DisplayName("[bad] 이름, 전화번호가 null일 때 에러를 반환한다.")
    @Test
    void nullFullNameTelNo() {
        //given
        FindEmailRequestDto dto = FindEmailRequestDto.of(null, null);

        //when
        Set<ConstraintViolation<FindEmailRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(2); // 이름, 전화번호가 둘다 null이므로
    }

    @DisplayName("[happy] 전화번호에 숫자가 아닌 다른 문자가 들어왔을때 에러를 반환한다.")
    @Test
    void telNoPatternFail() {
        //given
        FindEmailRequestDto dto = FindEmailRequestDto.of("fullName", "010-0000-1111");

        //when
        Set<ConstraintViolation<FindEmailRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1);  // 전화번호에 숫자가 아니라 다른 문자가 들어왔기 때문
    }


}