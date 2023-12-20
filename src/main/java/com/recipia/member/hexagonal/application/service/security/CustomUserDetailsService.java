package com.recipia.member.hexagonal.application.service.security;

import com.recipia.member.hexagonal.application.port.out.port.MemberPort;
import com.recipia.member.hexagonal.config.dto.SecurityUserDetailsDto;
import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberRepository;
import com.recipia.member.hexagonal.config.dto.TokenMemberInfoDto;
import com.recipia.member.hexagonal.domain.Member;
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

    private final MemberPort memberPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // email로 회원 있는지 조회
        Member member = memberPort.findMemberByEmail(email)
                .orElseThrow(
                        () -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND)
                );

        // Member entity to TokenMemberInfoDto
        TokenMemberInfoDto tokenMemberInfoDto = TokenMemberInfoDto.fromDomain(member);

        // 사용자 정보 기반으로 SecurityUserDetailsDto 객체 생성
        return new SecurityUserDetailsDto(tokenMemberInfoDto,
                Collections.singleton(new SimpleGrantedAuthority(
                        tokenMemberInfoDto.roleType().toString()
                )));
    }
}
