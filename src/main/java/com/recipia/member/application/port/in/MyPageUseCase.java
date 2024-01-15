package com.recipia.member.application.port.in;

import com.recipia.member.domain.MyPage;
import org.springframework.web.multipart.MultipartFile;

public interface MyPageUseCase {
    MyPage viewMyPage(Long memberId);
    Long updateMyPage(MyPage myPage, MultipartFile profileImage);
}
