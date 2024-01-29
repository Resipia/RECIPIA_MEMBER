package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.TempPasswordRequestDto;
import com.recipia.member.domain.TempPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@RequiredArgsConstructor
@Component
public class TempPasswordConverter {

    public TempPassword dtoToDomain(TempPasswordRequestDto dto) {
        return TempPassword.of(
                dto.getName(),
                dto.getTelNo(),
                dto.getEmail()
        );
    }
}
