package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.MyPage;

public interface MyPagePort {
    MyPage viewMyPage(Long memberId);
    Long updateMyPage(MyPage requestMyPage);
}
