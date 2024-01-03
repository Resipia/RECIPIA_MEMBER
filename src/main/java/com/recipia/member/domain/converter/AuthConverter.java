package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.CheckVerifyCodeRequestDto;
import com.recipia.member.adapter.in.web.dto.request.PhoneNumberRequestDto;
import com.recipia.member.domain.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@Component
public class AuthConverter {

    public Authentication phoneNumberRequestDtoToDomain(PhoneNumberRequestDto dto) {
        return Authentication.of(dto.getPhoneNumber(), null, null);
    }

    public Authentication checkVerifyCodeRequestDtoToDomain(CheckVerifyCodeRequestDto dto) {
        return Authentication.of(dto.getPhoneNumber(), null, dto.getVerifyCode());
    }


}
