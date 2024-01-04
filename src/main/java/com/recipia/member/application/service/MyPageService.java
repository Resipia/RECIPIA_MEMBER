package com.recipia.member.application.service;

import com.recipia.member.application.port.in.MyPageUseCase;
import com.recipia.member.application.port.out.port.MyPagePort;
import com.recipia.member.domain.MyPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyPageService implements MyPageUseCase {

    private final MyPagePort myPagePort;

    @Override
    public MyPage viewMyPage(MyPage myPage) {
        myPage = myPagePort.viewMyPage(myPage.getMemberId());
        return myPage;
    }
}
