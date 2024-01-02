package com.recipia.member.application.port.in;

import com.recipia.member.domain.Authentication;

public interface AuthUseCase {
    void verifyPhoneNumber(Authentication authentication);
    boolean checkVerifyCode(Authentication authentication);
}
