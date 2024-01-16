package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.EmailAvailableRequestDto;
import com.recipia.member.adapter.in.web.dto.request.ReportRequestDto;
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

//    @DisplayName("[happy] 올바른 EmailAvailableRequestDto로 요청 시 isEmailAvailable 호출")
//    @Test
//    void checkValidEmailWithValidRequest() throws Exception {
//        //given
//        EmailAvailableRequestDto dto = EmailAvailableRequestDto.of("email@naver.com");
//        when(memberManagementUseCase.isEmailAvailable(dto.getEmail())).thenReturn(true);
//        //when
//        mockMvc.perform(post("/member/management/checkDupEmail")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(dto)))
//                .andExpect(status().isOk())
//                .andDo(print());
//        //then
//        verify(memberManagementUseCase).isEmailAvailable(dto.getEmail());
//    }

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


    // JSON 문자열 변환을 위한 유틸리티 메서드
    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}