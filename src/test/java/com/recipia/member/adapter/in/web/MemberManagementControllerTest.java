package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.*;
import com.recipia.member.adapter.out.persistence.constant.ReportStatus;
import com.recipia.member.adapter.out.persistence.constant.ReportType;
import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Report;
import com.recipia.member.domain.converter.ReportConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("[통합] 회원 관리 컨트롤러 테스트")
class MemberManagementControllerTest extends TotalTestSupport {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private MemberManagementUseCase memberManagementUseCase;
    @MockBean
    private ReportConverter reportConverter;
    @MockBean
    private SecurityUtils securityUtils;

    @DisplayName("[happy] TelNoAvailableRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 중복체크를 검사한다.")
    @Test
    void shouldCheckTelNoWhenRequestDtoIsValid() throws Exception {
        //given
        TelNoAvailableRequestDto dto = TelNoAvailableRequestDto.of("01011111111");
        //when & then
        mockMvc.perform(post("/member/management/checkDupTelNo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @DisplayName("[happy] EmailAvailableRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 중복체크를 검사한다.")
    @Test
    void shouldCheckEmailWhenRequestDtoIsValid() throws Exception {
        //given
        EmailAvailableRequestDto dto = EmailAvailableRequestDto.of("email@naver.com");
        //when & then
        mockMvc.perform(post("/member/management/checkDupEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("[happy] 올바른 ReportRequestDto로 요청 시 reportMember 호출")
    @Test
    void requestReportWithValidRequest() throws Exception {
        //given
        ReportRequestDto dto = ReportRequestDto.of(3L, ReportType.SPAM, ""); // ReportRequestDto 객체 생성
        Report domain = Report.of(1L, 3L, ReportType.SPAM, "", ReportStatus.RECEIVED); // Report 객체 생성
        when(reportConverter.dtoToDomain(dto)).thenReturn(domain); // Mock 설정: dtoToDomain 호출 시 domain 반환
        when(memberManagementUseCase.reportMember(domain)).thenReturn(1L); // Mock 설정: reportMember 호출 시 1L 반환

        //when
        mockMvc.perform(post("/member/management/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto))) // API 요청 실행
                .andExpect(status().isOk()) // HTTP 상태 코드 200 확인
                .andDo(print()); // 응답 출력
    }

    @DisplayName("[happy] FindEmailRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 해당 회원의 이메일을 반환한다.")
    @Test
    void shouldReturnEmailWhenRequestDtoIsValid() throws Exception {
        //given
        FindEmailRequestDto dto = FindEmailRequestDto.of("fullName", "01000001111");
        //when & then
        mockMvc.perform(post("/member/management/find/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("[happy] TempPasswordRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 성공한다.")
    @Test
    void shouldSuccessWhenRequestDtoIsValid() throws Exception {
        //given
        TempPasswordRequestDto dto = TempPasswordRequestDto.of("fullName", "01011110000", "example@email.com");

        //when & then
        mockMvc.perform(post("/member/management/tempPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("[happy] NicknameAvailableRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 해당 닉네임의 중복 여부를 반환한다.")
    @Test
    void nicknameAvailable() throws Exception {
        // given
        NicknameAvailableRequestDto dto = NicknameAvailableRequestDto.of("hello");
        //when & then
        mockMvc.perform(post("/member/management/checkDupNickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("[happy] ChangePasswordRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 해당 닉네임의 중복 여부를 반환한다.")
    @Test
    void updatePassword() throws Exception {
        // given
        ChangePasswordRequestDto dto = ChangePasswordRequestDto.of("oldPassword", "passWord!@!1");
        //when & then
        mockMvc.perform(post("/member/management/updatePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("[happy] 인증 정보가 있는 회원이 요청하면 성공적으로 프로필 임시 url을 반환한다.")
    @Test
    void getProfileImage() throws Exception {
        // given
        when(securityUtils.getCurrentMemberId()).thenReturn(1L);
        MemberProfileRequestDto memberProfileRequestDto = MemberProfileRequestDto.of(1L);

        //when & then
        mockMvc.perform(post("/member/management/getProfile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(memberProfileRequestDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }


    // JSON 문자열 변환을 위한 유틸리티 메서드
    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}