package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.application.port.in.MyPageUseCase;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @BeforeEach
    void setup() {
        TestJwtConfig.setupMockAuthentication(1L, "user@example.com", "nickname");
    }


    @DisplayName("[happy] 마이페이지 조회 성공")
    @Test
    void whenAuthenticatedUserViewsMyPage_thenSuccess() throws Exception {
        // given
        MyPage requestMyPage = MyPage.of(1L);
        when(myPageUseCase.viewMyPage(requestMyPage)).thenReturn(any(MyPage.class));

        // when & then
        mockMvc.perform(get("/member/myPage/view"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}