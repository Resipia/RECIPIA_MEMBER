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
@RequestMapping("/user")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/getUser")
    public ResponseEntity<ResponseDto<MemberDto>> getUser(
            @RequestParam("userId") Long userId
    ) {

        MemberDto user = memberService.findMember(userId);
        return ResponseEntity.ok(ResponseDto.success(user));
    }



}
