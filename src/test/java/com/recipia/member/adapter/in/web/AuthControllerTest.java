package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.CheckVerifyCodeRequestDto;
import com.recipia.member.adapter.in.web.dto.request.PhoneNumberRequestDto;
import com.recipia.member.application.port.in.AuthUseCase;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.converter.AuthConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("[통합] 본인인증 요청/검증 컨트롤러 테스트")
class AuthControllerTest extends TotalTestSupport {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private AuthUseCase authUseCase;

    @MockBean
    private AuthConverter authConverter;

    @DisplayName("[happy] PhoneNumberRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 문자 메시지 전송 성공")
    @Test
    void shouldSendTextWhenPhoneNumberRequestDtoIsValid() throws Exception {
        //given
        PhoneNumberRequestDto phoneNumberRequestDto = createPhoneNumberRequestDtoRequiredField();
        //when & then
        mockMvc.perform(post("/member/auth/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(phoneNumberRequestDto)))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }


    @DisplayName("[bad] PhoneNumberRequestDto의 필수 필드값이 null일때 문자 메시지 전송 실패")
    @Test
    void shouldFailToSendTextWhenPhoneNumberRequestDtoFieldsAreNull() throws Exception {
        //given
        PhoneNumberRequestDto phoneNumberRequestDto = createPhoneNumberRequestDtoNull();
        //when & then
        mockMvc.perform(post("/member/auth/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(phoneNumberRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("phoneNumber");
                })
                .andExpect(result -> {
                    Throwable thrown = result.getResolvedException();
                    assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
                });
    }


    @DisplayName("[bad] PhoneNumberRequestDto의 필수 필드값이 공백일때 문자 메시지 전송 실패")
    @Test
    void shouldFailToSendTextWhenPhoneNumberRequestDtoFieldsAreBlank() throws Exception {
        //given
        PhoneNumberRequestDto phoneNumberRequestDto = createPhoneNumberRequestDtoBlank();
        //when & then
        mockMvc.perform(post("/member/auth/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(phoneNumberRequestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("phoneNumber");
                })
                .andExpect(result -> {
                    Throwable thrown = result.getResolvedException();
                    assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
                });
    }


    @DisplayName("[happy] CheckVerifyCodeRequestDto의 필수 필드값이 전부 채워진채로 요청이 들어오면 인증 코드 검증 성공")
    @Test
    void shouldVerifyCodeWhenCheckVerifyCodeRequestDtoIsValid() throws Exception {
        //given
        CheckVerifyCodeRequestDto requestDto = createCheckVerifyCodeRequestDtoRequiredField();

        //when & then
        mockMvc.perform(post("/member/auth/check/verifyCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @DisplayName("[bad] CheckVerifyCodeRequestDto의 필수 필드값이 전부 null일때 인증코드 검증 실패")
    @Test
    void shouldFailToVerifyCodeWhenCheckVerifyCodeRequestDtoFieldsAreAllNull() throws Exception {
        //given
        CheckVerifyCodeRequestDto requestDto = createCheckVerifyCodeRequestDtoAllNull();
        //when & then
        mockMvc.perform(post("/member/auth/check/verifyCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("phoneNumber");
                    assertThat(responseString).contains("verifyCode");
                })
                .andExpect(result -> {
                    Throwable thrown = result.getResolvedException();
                    assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
                });
    }


    @DisplayName("[bad] CheckVerifyCodeRequestDto의 필수 필드값이 전부 공백일때 인증코드 검증 실패")
    @Test
    void shouldFailToVerifyCodeWhenCheckVerifyCodeRequestDtoFieldsAreAllBlank() throws Exception {
        //given
        CheckVerifyCodeRequestDto requestDto = createCheckVerifyCodeRequestDtoAllBlank();
        //when & then
        mockMvc.perform(post("/member/auth/check/verifyCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("phoneNumber");
                    assertThat(responseString).contains("verifyCode");
                })
                .andExpect(result -> {
                    Throwable thrown = result.getResolvedException();
                    assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
                });
    }

    @DisplayName("[bad] CheckVerifyCodeRequestDto 필수 필드 중 하나가 Null일 때 인증코드 검증 실패")
    @Test
    void shouldFailToVerifyCodeWhenCheckVerifyCodeRequestDtoHasNullField() throws Exception {
        //given
        CheckVerifyCodeRequestDto requestDto = createCheckVerifyCodeRequestDtoOneNull();
        //when & then
        mockMvc.perform(post("/member/auth/check/verifyCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("phoneNumber");
                })
                .andExpect(result -> {
                    Throwable thrown = result.getResolvedException();
                    assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
                });
    }


    @DisplayName("[bad] CheckVerifyCodeRequestDto 필수 필드 중 하나가 공백일 때 인증코드 검증 실패")
    @Test
    void shouldFailToVerifyCodeWhenCheckVerifyCodeRequestDtoHasBlankField() throws Exception {
        //given
        CheckVerifyCodeRequestDto requestDto = createCheckVerifyCodeRequestDtoOneBlank();
        //when & then
        mockMvc.perform(post("/member/auth/check/verifyCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).contains("verifyCode");
                })
                .andExpect(result -> {
                    Throwable thrown = result.getResolvedException();
                    assertThat(thrown).isInstanceOf(MethodArgumentNotValidException.class);
                });
    }

    @DisplayName("[happy] 올바른 PhoneNumberRequestDto로 요청 시 verifyPhoneNumber 호출")
    @Test
    void verifyPhoneNumberCalledWithValidRequest() throws Exception {
        //given
        PhoneNumberRequestDto phoneNumberRequestDto = createPhoneNumberRequestDtoRequiredField();
        //when
        mockMvc.perform(post("/member/auth/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(phoneNumberRequestDto)))
                .andExpect(status().isOk())
                .andDo(print());
        //then
        verify(authUseCase).verifyPhoneNumber(authConverter.phoneNumberRequestDtoToDomain(phoneNumberRequestDto));
    }

    @DisplayName("[happy] 올바른 CheckVerifyCodeRequestDto로 요청 시 checkVerifyCode 호출")
    @Test
    void checkVerifyCodeCalledWithValidRequest() throws Exception {
        //given
        CheckVerifyCodeRequestDto requestDto = createCheckVerifyCodeRequestDtoRequiredField();
        when(authUseCase.checkVerifyCode(authConverter.checkVerifyCodeRequestDtoToDomain(requestDto))).thenReturn(true);
        //when
        mockMvc.perform(post("/member/auth/check/verifyCode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
        //then
        verify(authUseCase).checkVerifyCode(authConverter.checkVerifyCodeRequestDtoToDomain(requestDto));
    }

    private PhoneNumberRequestDto createPhoneNumberRequestDtoRequiredField() {
        return PhoneNumberRequestDto.of("01000001111");
    }

    private PhoneNumberRequestDto createPhoneNumberRequestDtoNull() {
        return PhoneNumberRequestDto.of(null);
    }

    private PhoneNumberRequestDto createPhoneNumberRequestDtoBlank() {
        return PhoneNumberRequestDto.of("   ");
    }


    private CheckVerifyCodeRequestDto createCheckVerifyCodeRequestDtoRequiredField() {
        return CheckVerifyCodeRequestDto.of("01011112222", "123456");
    }

    private CheckVerifyCodeRequestDto createCheckVerifyCodeRequestDtoAllNull() {
        return CheckVerifyCodeRequestDto.of(null, null);
    }

    private CheckVerifyCodeRequestDto createCheckVerifyCodeRequestDtoAllBlank() {
        return CheckVerifyCodeRequestDto.of("  ", "  ");
    }

    private CheckVerifyCodeRequestDto createCheckVerifyCodeRequestDtoOneNull() {
        return CheckVerifyCodeRequestDto.of(null, "123456");
    }

    private CheckVerifyCodeRequestDto createCheckVerifyCodeRequestDtoOneBlank() {
        return CheckVerifyCodeRequestDto.of("01011112222", "  ");
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