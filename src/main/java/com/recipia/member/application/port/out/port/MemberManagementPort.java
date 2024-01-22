package com.recipia.member.application.port.out.port;

public interface MemberManagementPort {
    boolean isEmailAvailable(String email);
    boolean isTelNoAvailable(String telNo);
    boolean isNicknameAvailable(String nickname);

}
