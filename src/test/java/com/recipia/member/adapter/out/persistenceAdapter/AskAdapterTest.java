package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Ask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("[통합] 문의사항 어댑터 테스트")
class AskAdapterTest extends TotalTestSupport {

    @Autowired
    private AskAdapter sut;

    @DisplayName("[happy] 유효한 문의사항 id가 들어왔을때 해당 문의사항의 상세 정보를 반환한다.")
    @Test
    void getAskDetail() {
        // given
        Ask domain = Ask.of(1L, 1L);
        // when
        Ask askDetail = sut.getAskDetail(domain);
        // then
        assertThat(askDetail).isNotNull();
    }

    @DisplayName("[happy] 존재하지 않는 ask id가 들어왔을때 에러를 발생시킨다.")
    @Test
    void getAskDetailFail() {
        // given
        Ask domain = Ask.of(99L, 1L);
        // then
        Exception exception = assertThrows(MemberApplicationException.class, () -> {
            sut.getAskDetail(domain);
        });

        // then
        // 예외가 실제로 발생했는지 확인한다. 예외 객체가 null이 아니어야 한다.
        assertNotNull(exception);
    }

}