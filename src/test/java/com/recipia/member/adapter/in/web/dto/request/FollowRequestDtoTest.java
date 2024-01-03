package com.recipia.member.adapter.in.web.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[단위] 팔로잉 요청 request dto 테스트")
class FollowRequestDtoTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("[happy] 팔로우 대상 멤버 id가 올바르게 제공되었을 때 성공한다.")
    @Test
    void validData() {
        //given
        FollowRequestDto dto = FollowRequestDto.of( 2L);

        //when
        Set<ConstraintViolation<FollowRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isEmpty();
    }

    @DisplayName("[bad] 팔로우 요청 멤버 id가 null일 때 에러를 반환한다.")
    @Test
    void nullEmail() {
        //given
        FollowRequestDto dto = FollowRequestDto.of(null);

        //when
        Set<ConstraintViolation<FollowRequestDto>> violations = validator.validate(dto);

        //then
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(1); // 팔로우 요청 멤버 id가 null이므로
    }


}