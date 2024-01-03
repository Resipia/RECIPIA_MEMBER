package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.FollowRequestDto;
import com.recipia.member.application.port.in.FollowUseCase;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.converter.FollowConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("[통합] 팔로우 컨트롤러 테스트")
class FollowControllerTest extends TotalTestSupport {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private FollowUseCase followUseCase;

    @MockBean
    private FollowConverter followConverter;

    @DisplayName("[happy] FollowRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 팔로우 요청 성공")
    @Test
    void shouldFollowSuccessWhenRequestDtoIsValid() throws Exception{
        // given
        FollowRequestDto dto = FollowRequestDto.of(3L);
        // when & then
        mockMvc.perform(post("/member/follow/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @DisplayName("[bad] FollowRequestDto의 팔로우 요청 대상 멤버 pk가 null일때 팔로우 요청 실패")
    @Test
    void shouldFailFollowWhenRequestDtoFieldsAreNull() throws Exception {
        // given
        FollowRequestDto dto = FollowRequestDto.of(null);
        //when & then
        mockMvc.perform(post("/member/follow/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("followingMemberId");
                })
                .andExpect(result -> {
                    Throwable thrown = result.getResolvedException();
                    assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
                });

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