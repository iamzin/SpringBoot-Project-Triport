package com.project.triport.controller;

import com.project.triport.requestDto.MemberInfoRequestDto;
import com.project.triport.requestDto.MemberProfileRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto> updateMemberInfo(@ModelAttribute MemberProfileRequestDto memberProfileRequestDto) throws IOException {
        return ResponseEntity.ok(memberService.updateMember(memberProfileRequestDto));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ResponseDto> deleteMember() {
        return ResponseEntity.ok(memberService.deleteMember());
    }
}
