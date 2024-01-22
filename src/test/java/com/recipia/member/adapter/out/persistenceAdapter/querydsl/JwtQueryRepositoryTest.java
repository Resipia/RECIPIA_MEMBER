package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.out.persistenceAdapter.JwtRepository;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("[통합] JWT query repository 테스트")
@Transactional
class JwtQueryRepositoryTest extends TotalTestSupport {

    @Autowired
    private JwtQueryRepository sut;
    @Autowired
    private JwtRepository jwtRepository;

    @DisplayName("[happy] 존재하는 회원의 email로 JWT를 삭제한다.")
    @Test
    void deleteByMemberEmail() {
        // given
        String email = "hong1@example.com";
        // when
        sut.deleteByMemberEmail(email);
        // then
        boolean isExist = jwtRepository.existsByMemberId(1L);
        assertFalse(isExist);
    }


}