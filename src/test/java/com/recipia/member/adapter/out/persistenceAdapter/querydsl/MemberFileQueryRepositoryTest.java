package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.out.persistence.MemberFileEntity;
import com.recipia.member.adapter.out.persistenceAdapter.MemberFileRepository;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("[happy] 프로필 이미지가 있는 회원의 memberId로 프로필 저장 경로를 반환한다.")
    @Test
    void getValidProfilePath() {
        // given
        Long memberId = 1L;
        // when
        String fileFullPath = sut.getFileFullPath(memberId);
        // then
        assertThat(fileFullPath).isNotNull();
    }

    @DisplayName("[happy] 프로필 이미지가 없는 회원의 memberId로 요청할때 null을 반환한다.")
    @Test
    void getNullProfilePath() {
        // given
        Long memberId = 4L;
        // when
        String fileFullPath = sut.getFileFullPath(memberId);
        // then
        assertThat(fileFullPath).isNull();
    }

}