//package com.project.triport.controller;
//
//import com.project.triport.service.AuthKakaoService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthKakaoController {
//    private final AuthKakaoService authKakaoService;
//
//    // Kakao 로그인
//    @GetMapping("/kakao/callback")
//    public String kakaoLogin(String code) {
//        // authorizedCode: kakao 서버로부터 받은 인가 코드
//        authKakaoService.kakaoLogin(code);
//        return "redirect:/";
//    }
//}
