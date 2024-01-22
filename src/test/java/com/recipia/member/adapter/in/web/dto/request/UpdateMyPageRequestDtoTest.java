package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] 마이페이지 수정 요청 dto 테스트")
class UpdateMyPageRequestDtoTest {

    // Java의 Bean Validation API를 사용해 유효성 검증을 수행하기 위한 Validator 객체를 생성
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @DisplayName("[happy] 필수값이 전부 들어왔을때 성공한다.")
    @Test
    void requestValidateSuccess() {
        // given
        UpdateMyPageRequestDto dto = UpdateMyPageRequestDto.of("update-nickname", "update-introduce", "2020-02-02", "M");

        // when
        // ConstraintViolation은 유효성 검사에서 발견된 제약 조건 위반을 나타내는 클래스다.
        Set<ConstraintViolation<UpdateMyPageRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();
    }


    @DisplayName("[bad] 필수값이 누락되었을때 에러를 반환한다.")
    @Test
    void requestValidateFail() {
        // given
        UpdateMyPageRequestDto dto = UpdateMyPageRequestDto.of("", "update-introduce", "2020-02-02", "M");

        // when
        // ConstraintViolation은 유효성 검사에서 발견된 제약 조건 위반을 나타내는 클래스다.
        Set<ConstraintViolation<UpdateMyPageRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // nickname이 빠졌기 때문

    }
}