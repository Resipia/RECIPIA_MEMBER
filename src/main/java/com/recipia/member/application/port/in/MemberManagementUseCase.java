package com.recipia.member.application.port.in;

public interface MemberManagementUseCase {
    boolean isEmailAvailable(String email);
    boolean isTelNoAvailable(String telNo);
}
