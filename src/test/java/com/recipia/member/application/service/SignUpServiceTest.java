package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.adapter.out.persistenceAdapter.SignUpAdapter;
import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @DisplayName("[happy] 프로필 이미지가 없을때 정상적으로 회원가입이 진행된다.")
//    @Test
//    void signUp_Success() {
//        // Given
//        Member member = Member.of("email@naver.com", "passWord12!@", "fullname", "nickname", MemberStatus.ACTIVE, "intro", "1010", "add1", "add2", RoleType.MEMBER);
//
//        MultipartFile profileImage = mock(MultipartFile.class);
//        when(profileImage.isEmpty()).thenReturn(true);
//        when(signUpPort.signUpMember(any(Member.class))).thenReturn(1L);
//
//        // When
//        Long result = sut.signUp(member, profileImage);
//
//        // Then
//        assertEquals(1L, result);
//    }


}