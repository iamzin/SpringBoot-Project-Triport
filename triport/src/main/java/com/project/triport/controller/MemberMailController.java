package com.project.triport.controller;

import com.project.triport.requestDto.MemberMailRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.MemberMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@Controller
//@AllArgsConstructor
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MemberMailController {
    private final MemberMailService memberMailService;

    @PostMapping("/reset/password")
    public ResponseEntity<ResponseDto> sendTempPwd(@RequestBody MemberMailRequestDto memberMailRequestDto) {
        return ResponseEntity.ok(memberMailService.sendTempPwd(memberMailRequestDto));
    }

}
