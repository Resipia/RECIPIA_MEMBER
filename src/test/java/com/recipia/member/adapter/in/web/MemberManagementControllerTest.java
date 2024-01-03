package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.EmailAvailableRequestDto;
import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

    @SpyBean
    private MemberManagementUseCase memberManagementUseCase;


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
                .andDo(print())
        ;
    }

    @DisplayName("[happy] 올바른 EmailAvailableRequestDto로 요청 시 isEmailAvailable 호출")
    @Test
    void checkValidEmailWithValidRequest() throws Exception {
        //given
        EmailAvailableRequestDto dto = EmailAvailableRequestDto.of("email@naver.com");
        when(memberManagementUseCase.isEmailAvailable(dto.getEmail())).thenReturn(true);
        //when
        mockMvc.perform(post("/member/management/checkDupEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
        //then
        verify(memberManagementUseCase).isEmailAvailable(dto.getEmail());

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