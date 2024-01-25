package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.adapter.out.persistenceAdapter.MemberFileRepository;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[통합] 멤버 파일 querydsl 테스트")
class MemberFileQueryRepositoryTest extends TotalTestSupport {
    @Autowired
    private MemberFileQueryRepository sut;
    @Autowired
    private MemberFileRepository memberFileRepository;

    @DisplayName("[happy] memberId에 해당하는 프로필 이미지가 존재하면 삭제처리한다.")
    @Test
    void softDeleteProfileImageByMemberId() {
        // given
        Long memberId = 1L;
        // when
        sut.softDeleteProfileImageByMemberId(memberId);
        // then
        Optional<MemberFileEntity> memberFileEntity = memberFileRepository.findByMemberEntity_Id(memberId);
        assertEquals(memberFileEntity.get().getDelYn(), "Y");
    }

}