package com.recipia.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@DisplayName("멤버 리포지토리 단위테스트")
@Transactional(readOnly = true)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

//    @DisplayName("데이터베이스에 멤버 엔티티를 저장하면 저장성공한 엔티티를 반환받는다.")
//    @Transactional
//    @Test
//    void memberSave() {
//        //given
//        Member member = createMember();
//
//        //when
//        Member savedMember = memberRepository.save(member);
//
//        //then
//        assertThat(savedMember).isNotNull();
//        assertThat(savedMember).isInstanceOf(Member.class);
//
//    }


//    private Member createMember() {
//        return Member.of(
//                "yjkim",
//                "asdf1234",
//                "yijun kim",
//                "king whang zzhang",
//                "hello, this is yijun",
//                "010-0000-0000",
//                "서울",
//                "성동구",
//                "test@gmail.com",
//                "Y",
//                "Y"
//        );
//    }

}