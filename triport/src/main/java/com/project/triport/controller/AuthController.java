package com.project.triport.controller;

import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.requestDto.TokenRequestDto;
import com.project.triport.responseDto.MemberInfoResponseDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.responseDto.TokenDto;
import com.project.triport.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.signup(memberRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<MemberInfoResponseDto> login(@RequestBody MemberRequestDto memberRequestDto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(memberRequestDto, response));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
