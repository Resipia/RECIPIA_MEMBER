package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.SignUpRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member")
@RestController
public class SignUpController {

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto<Long>> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {

        return ResponseEntity.ok(
                ResponseDto.success(1L)
        );
    }

}
