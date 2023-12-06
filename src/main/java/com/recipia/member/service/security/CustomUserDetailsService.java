package com.recipia.member.service.security;

import com.recipia.member.config.dto.SecurityUserDetailsDto;
import com.recipia.member.hexagonal.adapter.out.persistence.member.Member;
import com.recipia.member.dto.MemberDto;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username으로 회원 있는지 조회
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(
                        () -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND)
                );

        // Member entity to MemberDto
        MemberDto memberDto = MemberDto.fromEntity(member);

        // 사용자 정보 기반으로 SecurityUserDetailsDto 객체 생성
        return new SecurityUserDetailsDto(memberDto,
                // todo: 추후에는 memberDto.roleType().toString()로 수정
                Collections.singleton(new SimpleGrantedAuthority(
                        "MEMBER"
                )));
    }
}
