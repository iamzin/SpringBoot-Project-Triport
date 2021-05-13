package com.project.triport.controller;

import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.AuthKakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthKakaoController {
    private final AuthKakaoService authKakaoService;

    // Kakao 로그인
    @GetMapping("/kakao/callback")
    public ResponseEntity<ResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        // authorizedCode: kakao 서버로부터 받은 인가 코드
        return ResponseEntity.ok(authKakaoService.kakaoLogin(code, response));
    }
}
