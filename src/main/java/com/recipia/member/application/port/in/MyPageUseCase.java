package com.recipia.member.application.port.in;

import com.recipia.member.domain.MyPage;

public interface MyPageUseCase {
    MyPage viewMyPage(Long memberId);
    MyPage updateAndViewMyPage(MyPage myPage);
}
