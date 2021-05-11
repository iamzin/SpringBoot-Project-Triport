package com.project.triport.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakao")
public class KakaoController {

    @GetMapping()
    public @ResponseBody String KakoCallback(String code) {
        return "kakao code: " + code;
    }
}
