package com.project.triport.controller;

import com.project.triport.requestDto.MailRequestDto;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.responseDto.MailResponseDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.MailService;
import com.project.triport.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseDto> getMemberInfo() {
        return ResponseEntity.ok(memberService.getMember());
    }

    @PostMapping("/profile")
    public ResponseEntity<ResponseDto> updateMemberInfo(@ModelAttribute MemberRequestDto memberRequestDto) throws IOException {
        return ResponseEntity.ok(memberService.updateMember(memberRequestDto));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteMember() {
        return ResponseEntity.ok(memberService.deleteMember());
    }
}
