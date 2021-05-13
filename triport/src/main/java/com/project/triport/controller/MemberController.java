package com.project.triport.controller;

import com.project.triport.requestDto.MailRequestDto;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.responseDto.MailResponseDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.service.MailService;
import com.project.triport.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MailService mailService;

    @GetMapping("/profile")
    public ResponseEntity<MemberResponseDto> getMemberInfo() {
        return ResponseEntity.ok(memberService.getMember());
    }

    @PutMapping("/profile")
    public ResponseEntity<MemberResponseDto> updateMemberInfo(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.updateMember(memberRequestDto));
    }

//    @PostMapping("/reset/password")
//    public ResponseEntity<MailResponseDto> sendTempPwd(@RequestBody MailRequestDto mailRequestDto) {
//        return ResponseEntity.ok(mailService.sendTempPwd(mailRequestDto));
//    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteMember() {
        return ResponseEntity.ok(memberService.deleteMember());
    }

}
