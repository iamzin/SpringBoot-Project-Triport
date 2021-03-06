package com.project.triport.controller;

import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.AuthBasicService;
import com.project.triport.service.AuthKakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthKakaoController {
    private final AuthKakaoService authKakaoService;
    private final AuthBasicService authBasicService;

    // Kakao 로그인
    @GetMapping("/kakao/callback")
    public ResponseEntity<ResponseDto> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        // authorizedCode: kakao 서버로부터 받은 인가 코드
        return ResponseEntity.ok(authKakaoService.kakaoLogin(code, response));
    }

//    @GetMapping("/logout")
//    public ResponseEntity<ResponseDto> Logout() {
//        return ResponseEntity.ok(authBasicService.logout());
//    }
}
