package com.recipia.member.application.port.in;

import com.recipia.member.domain.Authentication;
import com.recipia.member.domain.Logout;

public interface AuthUseCase {
    void verifyPhoneNumber(Authentication authentication);
    boolean checkVerifyCode(Authentication authentication);
    void logout(Logout logout);
    Long deactivateMember(Long memberId);
}
