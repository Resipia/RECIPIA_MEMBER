package com.recipia.member.service;


import com.recipia.member.domain.Member;
import com.recipia.member.event.NicknameChangeEvent;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void nicknameChage() {
        Member member = memberRepository.findById(2L).orElseThrow(() -> new MemberApplicationException(ErrorCode.DB_ERROR));
        member.changeNickname("NEW-NICKNAME222");

        log.info("닉네임 변경 Service [멤버 pk : {} , 변경전 닉네임 : {} , 변경할 닉네임 : {}]", member.getId(), "oldNickname", "NEW-NICKNAME222");
        eventPublisher.publishEvent(new NicknameChangeEvent(member.getId()));
    }

}



