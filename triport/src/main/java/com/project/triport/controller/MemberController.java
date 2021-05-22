package com.project.triport.controller;

import com.project.triport.requestDto.MemberProfileImgRequestDto;
import com.project.triport.requestDto.MemberProfileInfoRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

    @PostMapping("/profile/info")
    public ResponseEntity<ResponseDto> updateMemberProfileInfo(@RequestBody @Valid MemberProfileInfoRequestDto memberProfileInfoRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberProfileInfo(memberProfileInfoRequestDto));
    }

    @PostMapping("/profile/img")
    public ResponseEntity<ResponseDto> updateMemberProfileImg(@ModelAttribute MemberProfileImgRequestDto memberProfileImgRequestDto) throws IOException {
        return ResponseEntity.ok(memberService.updateMemberProfileImg(memberProfileImgRequestDto));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ResponseDto> deleteMember() {
        return ResponseEntity.ok(memberService.deleteMember());
    }
}
