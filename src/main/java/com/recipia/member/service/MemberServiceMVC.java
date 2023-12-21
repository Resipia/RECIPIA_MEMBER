package com.recipia.member.service;


import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.common.event.NicknameChangeSpringEvent;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.adapter.out.persistenceAdapter.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceMVC {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void nicknameChange() {
        MemberEntity member = memberRepository.findById(2L).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        member.changeNickname("NEW-NICKNAME222");

        eventPublisher.publishEvent(new NicknameChangeSpringEvent(member.getId()));
    }

}



