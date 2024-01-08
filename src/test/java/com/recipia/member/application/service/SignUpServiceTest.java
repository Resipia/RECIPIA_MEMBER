package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
@DisplayName("[단위] 회원가입 Service 테스트")
class SignUpServiceTest {

    @Mock
    private SignUpPort signUpPort;
    @Mock
    private ImageS3Service imageS3Service;
    @InjectMocks
    private SignUpService sut;

    @Test
    void signUpTestWithoutProfileImage() {
        // 멤버 객체 생성 및 유효한 정보 설정
        Member member = Member.of("email@naver.com", "Passworddf12!", "fullname", "nickname", MemberStatus.ACTIVE, "intro", "1010", "add1", "add2", RoleType.MEMBER);

        // Mock 객체의 행동 정의
        when(signUpPort.signUpMember(any(Member.class))).thenReturn(1L); // 성공적인 회원 가입 가정

        // 테스트 실행
        Long result = sut.signUp(member, null);

        // 결과 검증
        assertEquals(1L, result);

        // 검증: signUpPort.signUpMember가 정확히 한 번 호출되었는지 확인
        verify(signUpPort, times(1)).signUpMember(any(Member.class));

        // 검증: 프로필 이미지가 null이므로, imageS3Service.createMemberFile가 호출되지 않았는지 확인
        verify(imageS3Service, never()).createMemberFile(any(MultipartFile.class), anyInt(), anyLong());
    }

}