package com.recipia.member.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.adapter.in.web.dto.request.UpdateMyPageRequestDto;
import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.MyPageViewResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

//
//    @DisplayName("[happy] 인증된 사용자가 마이페이지 조회")
//    @Test
//    void whenAuthenticatedUserViewsMyPage_thenSuccess() throws Exception {
//        // given
//        Long memberId = 1L;
//        MyPage myPage = MyPage.of(memberId);
//        MyPageViewResponseDto dto = MyPageViewResponseDto.of(1L, "url", "nick", "intro", 3L, 4L, "2020-02-02", "M");
//        when(securityUtils.getCurrentMemberId()).thenReturn(memberId);
//        when(myPageUseCase.viewMyPage(memberId)).thenReturn(myPage);
//        when(myPageConverter.domainToResponseDto(myPage)).thenReturn(dto);
//
//        // when & then
//        mockMvc.perform(post("/member/myPage/view"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @DisplayName("[happy] 마이페이지 수정 요청 성공")
    @Test
    void whenAuthenticatedUserRequestsMyPageUpdate_thenSuccess() throws Exception {
        // given
        MockMultipartFile mockFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        UpdateMyPageRequestDto dto = UpdateMyPageRequestDto.of("nickname");

        MyPage myPage = MyPage.builder().memberId(1L).nickname(dto.getNickname()).build();

        when(myPageConverter.updateRequestDtoToDomain(dto)).thenReturn(myPage);

        // when & then
        mockMvc.perform(multipart("/member/myPage/update")
                        .file(mockFile)
                        .flashAttr("updateMyPageRequestDto", dto))
                .andExpect(status().isOk());
    }

    @DisplayName("[happy] targetMemberId에 해당하는 회원의 팔로우 목록을 요청했을때 정상적으로 페이징된 데이터를 반환한다.")
    @Test
    void getFollowingListSuccess() throws Exception {
        // given
        FollowListResponseDto dto = FollowListResponseDto.of(1L, "pre", "nickname", null, false);
        PagingResponseDto<FollowListResponseDto> pagingResponseDto = PagingResponseDto.of(List.of(dto), 100L);
        // when
        when(myPageUseCase.getFollowList(anyLong(), anyString(),anyInt(), anyInt())).thenReturn(pagingResponseDto);

        // then
        mockMvc.perform(get("/member/myPage/followList")
                        .param("page", "0")
                        .param("size", "10")
                        .param("targetMemberId", "1")
                        .param("type", "follow")
                ).andExpect(status().isOk())
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