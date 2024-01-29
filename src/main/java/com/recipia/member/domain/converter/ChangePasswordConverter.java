package com.recipia.member.domain.converter;

import com.recipia.member.adapter.in.web.dto.request.ChangePasswordRequestDto;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.ChangePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChangePasswordConverter {

    private final SecurityUtils securityUtils;

    public ChangePassword dtoToDomain(ChangePasswordRequestDto dto) {
        return ChangePassword.of(securityUtils.getCurrentMemberId(), dto.getOriginPassword(), dto.getNewPassword());
    }

}
