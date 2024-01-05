package com.recipia.member.application.port.in;

import com.recipia.member.domain.MyPage;

public interface MyPageUseCase {
    MyPage viewMyPage(MyPage myPage);
    MyPage updateAndViewMyPage(MyPage myPage);
}
