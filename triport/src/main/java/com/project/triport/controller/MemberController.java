package com.project.triport.controller;

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

    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getMyMemberInfo() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @GetMapping("/{email}") // url로 주는 이유?
    public ResponseEntity<MemberResponseDto> getMemberInfo(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getMemberInfo(email));
    }

//    @GetMapping("/email") // url로 주는게 Reuqestbody 보다 안전한건가?
//    public ResponseEntity<MemberResponseDto> getMemberInfo(@RequestBody MemberRequestDto memberRequestDto) {
//        return ResponseEntity.ok(memberService.getMemberInfo(memberRequestDto.getEmail()));
//    }
}
