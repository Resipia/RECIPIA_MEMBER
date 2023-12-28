package com.recipia.member.application.port.in;

import com.recipia.member.domain.Authentication;

public interface AuthUseCase {
    void verityPhoneNumber(Authentication authentication);
}
