package com.recipia.member.controller;

import com.recipia.member.dto.MemberDto;
import com.recipia.member.dto.response.ResponseDto;
import com.recipia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;

//    @GetMapping("/getMember")
//    public ResponseEntity<ResponseDto<String>> getMember(
//            @RequestParam("username") String username
//    ) {
//
//        String result = memberService.memberUpdateEventPublish(username);
//        return ResponseEntity.ok(ResponseDto.success(result));
//    }

    @GetMapping("/test")
    public String test() {
        return "aws test success";
    }



}
