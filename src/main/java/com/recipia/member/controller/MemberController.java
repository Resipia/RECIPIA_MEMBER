package com.recipia.member.controller;

import com.recipia.member.service.MemberServiceMVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberServiceMVC memberServiceMVC;

    @PostMapping("/nicknameChange")
    public String nicknameChange() {

        memberServiceMVC.nicknameChange();
        return "success";
    }

}
