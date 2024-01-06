package com.recipia.member.application.port.in;

import com.recipia.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

public interface SignUpUseCase {
    Long signUp(Member member, MultipartFile profileImage);
}
