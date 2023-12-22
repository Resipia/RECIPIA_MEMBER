package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.SignUpRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// fixme: 통합으로 수정
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class SignUpControllerTest{

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("[happy] 필수 입력값이 전부 충족된 dto가 들어왔을때 회원가입 정상 작동한다")
    @Test
    void test() throws Exception {
        //given
        SignUpRequestDto validRequest = SignUpRequestDto.of(
                "user@example.com", "password123", "John Doe", "johndoe",
                "Hello, I'm John", "010-1234-5678", "123 Main St", "Apt 101",
                "Y", "Y", "Y", "Y"
        );

        //when & then

        mockMvc.perform(post("/member/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(validRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("[fail] 필수 입력값이 누락된 dto가 들어왔을 때 BadRequest를 반환한다")
    void testMissingRequiredFields() throws Exception {
        //given
        SignUpRequestDto invalidRequest = SignUpRequestDto.of(
                null, "password123", null, "johndoe",
                "Hello, I'm John", "010-1234-5678", "123 Main St", "Apt 101",
                "Y", "Y", "Y", "Y"
        );

        //when & then
        mockMvc.perform(post("/member/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("email");
                    assertThat(responseString).contains("fullName");
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