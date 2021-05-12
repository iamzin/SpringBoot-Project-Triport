package com.project.triport.controller;

import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.requestDto.TokenRequestDto;
import com.project.triport.responseDto.MemberInfoResponseDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.service.AuthBasicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthBasicController {
    private final AuthBasicService authBasicService;

    // 기본 회원가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authBasicService.signup(memberRequestDto));
    }

    // 기본 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberInfoResponseDto> login(@RequestBody MemberRequestDto memberRequestDto,
                                                       HttpServletResponse response) {
        return ResponseEntity.ok(authBasicService.login(memberRequestDto, response));
    }

    // 기본 token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<MemberInfoResponseDto> reissue(@RequestBody TokenRequestDto tokenRequestDto,
                                                         HttpServletResponse response) {
        return ResponseEntity.ok(authBasicService.reissue(tokenRequestDto, response));
    }
}
