package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.UpdateMyPageRequestDto;
import com.recipia.member.adapter.in.web.dto.response.MyPageViewResponseDto;
import com.recipia.member.application.port.in.MyPageUseCase;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.config.TestJwtConfig;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.MyPage;
import com.recipia.member.domain.converter.MyPageConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("[통합] 마이페이지 컨트롤러 테스트")
class MyPageControllerTest extends TotalTestSupport {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private MyPageUseCase myPageUseCase;

    @MockBean
    private MyPageConverter myPageConverter;
    @MockBean
    private SecurityUtils securityUtils;

    @BeforeEach
    void setup() {
        TestJwtConfig.setupMockAuthentication(1L, "user@example.com", "nickname");
    }


    @DisplayName("[happy] 인증된 사용자가 마이페이지 조회")
    @Test
    void whenAuthenticatedUserViewsMyPage_thenSuccess() throws Exception {
        // given
        Long memberId = 1L;
        MyPage myPage = MyPage.of(memberId);
        MyPageViewResponseDto dto = MyPageViewResponseDto.of(1L, "url", "nick", "intro", 3L, 4L, "2020-02-02", "M");
        when(securityUtils.getCurrentMemberId()).thenReturn(memberId);
        when(myPageUseCase.viewMyPage(memberId)).thenReturn(myPage);
        when(myPageConverter.domainToResponseDto(myPage)).thenReturn(dto);

        // when & then
        mockMvc.perform(post("/member/myPage/view"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[happy] 마이페이지 수정 요청 성공")
    @Test
    void whenAuthenticatedUserRequestsMyPageUpdate_thenSuccess() throws Exception {
        // given
        UpdateMyPageRequestDto dto = UpdateMyPageRequestDto.builder().nickname("hello").build();
        MyPage myPage = MyPage.builder().memberId(1L).nickname(dto.getNickname()).build();

        when(myPageConverter.updateRequestDtoToDomain(dto)).thenReturn(myPage);

        // when & then
        mockMvc.perform(post("/member/myPage/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
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