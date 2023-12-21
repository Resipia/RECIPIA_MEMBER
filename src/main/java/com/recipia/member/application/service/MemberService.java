package com.recipia.member.application.service;

import com.recipia.member.application.port.in.MemberUseCase;
import com.recipia.member.application.port.out.port.MemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService implements MemberUseCase {

    private final MemberPort memberPort;
}
