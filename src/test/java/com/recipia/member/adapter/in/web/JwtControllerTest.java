package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.JwtRepublishRequestDto;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[통합] JWT 컨트롤러 테스트")
@AutoConfigureMockMvc
class JwtControllerTest extends TotalTestSupport {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();


    @DisplayName("[happy] requestDto에 필수 입력값이 전부 들어왔을때 access token 재발행 성공")
    @Test
    void fulfillRequiredFields() throws Exception {
        //given
        JwtRepublishRequestDto jwtRepublishRequest = createRequestTotalField();
        //when & then
        mockMvc.perform(post("/member/jwt/republish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jwtRepublishRequest)))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @DisplayName("[bad] 만약 requestDto에 필수 입력값(필드)이 누락된 채로 토큰 재발행이 요청되면 응답 내부에 그 필드값이 기록되고 " +
            "MethodArgumentNotValidException 에러가 발생하면서 access token 재발행에 실패한다.")
    @Test
    void missingRequiredFields() throws Exception {
        //given
        JwtRepublishRequestDto jwtRepublishRequest = createRequestMissingRequiredField();
        //when & then
        mockMvc.perform(post("/member/jwt/republish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jwtRepublishRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("memberId");
                    assertThat(responseString).contains("refreshToken");
                })
                .andExpect(result -> {
                    Throwable thrown = result.getResolvedException();
                    assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
                });
    }

    private JwtRepublishRequestDto createRequestTotalField() {
        return JwtRepublishRequestDto.of(1L, "some-refresh-token");
    }

    private JwtRepublishRequestDto createRequestMissingRequiredField() {
        return JwtRepublishRequestDto.of(null, " ");
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