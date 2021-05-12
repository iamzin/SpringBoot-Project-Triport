package com.project.triport.controller;

import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/profile")
    public ResponseEntity<MemberResponseDto> getMemberInfo() {
        return ResponseEntity.ok(memberService.getMemberInfo());
    }

    @PutMapping("/profile")
    public ResponseEntity<MemberResponseDto> updateMemberInfo(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberInfo(memberRequestDto));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteMember() {
        return ResponseEntity.ok(memberService.deleteMember());
    }

}
