package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.PhoneNumberRequestDto;
import com.recipia.member.domain.Authentication;
import lombok.RequiredArgsConstructor;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@RequiredArgsConstructor
public class AuthConverter {

    public static Authentication requestToDomain(PhoneNumberRequestDto dto) {
        return Authentication.of(dto.getPhoneNumber(), null);
    }

//    public static Authentication domainToResponse(Authentication domain) {
//
//    }

}
