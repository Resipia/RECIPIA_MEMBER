package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.out.aws.TokyoSnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final TokyoSnsService tokyoSnsService;

    @PostMapping("/phone")
    public void authPhoneNumber() {
        String phoneNumbner = "+8201099984653";
        tokyoSnsService.sendVerificationCode(phoneNumbner);

    }

}
