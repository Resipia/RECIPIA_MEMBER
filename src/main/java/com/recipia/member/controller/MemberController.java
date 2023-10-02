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

    @GetMapping("/getMember")
    public ResponseEntity<ResponseDto<MemberDto>> getMember(
            @RequestParam("username") String username
    ) {

        MemberDto member = memberService.findMember(username);
        return ResponseEntity.ok(ResponseDto.success(member));
    }

    // todo: member의 nickname이 변경되면 recipe서버나 다른 서버에 kafka이벤트를 발행해서 보낸다.




}
