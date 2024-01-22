package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.SignUpRequestDto;
import com.recipia.member.application.port.in.SignUpUseCase;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.converter.MemberConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[통합] 회원가입 controller 테스트")
@AutoConfigureMockMvc
class SignUpControllerTest extends TotalTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignUpUseCase signUpUseCase;
    @MockBean
    private MemberConverter memberConverter;
    private ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("[happy] 필수 입력값이 전부 충족된 dto가 들어왔을때 회원가입 정상 작동한다")
    @Test
    void fulfillRequiredFields() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.of(
                "user@example.com", "password123P!", "John Doe", "johndoe",
                "Hello, I'm John", "01012345678", "123 Main St", "Apt 101", "Y", "Y"
        );
        Member member = Member.of(1L);
        when(signUpUseCase.signUp(member, mockFile)).thenReturn(1L);

        //when & then
        mockMvc.perform(multipart("/member/signUp")
                        .file(mockFile)
                        .flashAttr("signUpRequestDto", signUpRequestDto))
                .andExpect(status().isOk());
    }



    @Test
    @DisplayName("[bad] 필수 입력값이 누락된 dto가 들어왔을 때 BadRequest를 반환한다")
    void testMissingRequiredFields() throws Exception {
        //given
        MultipartFile mockFile = mock(MultipartFile.class);
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.of(
                " ", "password123P!", "", "johndoe",
                "Hello, I'm John", "01012345678", "123 Main St", "Apt 101", "Y", "Y"
        );
        Member member = Member.of(1L);
        when(signUpUseCase.signUp(member, mockFile)).thenReturn(1L);

        //when & then
        mockMvc.perform(post("/member/signUp")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("signUpRequestDto", signUpRequestDto))
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("email");
                    assertThat(responseString).contains("fullName");
                });
    }


    @Test
    @DisplayName("[bad] 비밀번호 정규식에 맞지 않는 비밀번호가 들어왔을때 회원가입에 실패한다.")
    void passwordRegNotFullfill() throws Exception {
        //given
        SignUpRequestDto invalidRequest = SignUpRequestDto.of(
                null, "password123", null, "johndoe",
                "Hello, I'm John", "01012345678", "123 Main St", "Apt 101", "Y", "Y"
        );


        //when & then
        mockMvc.perform(post("/member/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidRequest)))
                .andExpect(status().isBadRequest())
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