package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.AskRequestDto;
import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.application.port.in.AskUseCase;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.converter.AskConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("[통합] 문의사항 컨트롤러 테스트")
class AskControllerTest extends TotalTestSupport {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AskUseCase askUseCase;
    @MockBean
    private AskConverter askConverter;
    private ObjectMapper objectMapper = new ObjectMapper();


    @DisplayName("[happy] 유효한 요청이 들어왔을때 성공한다.")
    @Test
    void createAsk() throws Exception {
        // given
        AskRequestDto dto = AskRequestDto.of("title", "content");
        //when & then
        mockMvc.perform(post("/member/ask/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andDo(print())
        ;

    }

    @DisplayName("[happy] 내가 작성한 문의사항 목록을 요청했을때 정상적으로 페이징된 데이터를 반환한다.")
    @Test
    void getList() throws Exception {
        // given
        AskListResponseDto dto = AskListResponseDto.of(1L, "title", true, "2020-22-22");
        PagingResponseDto<AskListResponseDto> pagingResponseDto = PagingResponseDto.of(List.of(dto), 1L);

        // when
        when(askUseCase.getAskList(anyInt(), anyInt())).thenReturn(pagingResponseDto);
        // then
        mockMvc.perform(get("/member/ask/list")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.totalCount").value(pagingResponseDto.getTotalCount()));
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