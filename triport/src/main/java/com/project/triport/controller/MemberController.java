package com.project.triport.controller;

import com.project.triport.requestDto.MemberImgRequestDto;
import com.project.triport.requestDto.MemberProfileRequestDto;
import com.project.triport.requestDto.MemberNicknameRequestDto;
import com.project.triport.requestDto.MemberPwdRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BoardService;
import com.project.triport.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/profile")
    public ResponseEntity<ResponseDto> getMemberInfo() {
        return ResponseEntity.ok(memberService.getMember());
    }

    @PostMapping("/profile/nickname")
    public ResponseEntity<ResponseDto> updateMemberNickname(@RequestBody @Valid MemberNicknameRequestDto memberNicknameRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberNickname(memberNicknameRequestDto));
    }

    @PostMapping("/profile/pwd")
    public ResponseEntity<ResponseDto> updateMemberPwd(@RequestBody @Valid MemberPwdRequestDto memberPwdRequestDto) {
        return ResponseEntity.ok(memberService.updateMemberPwd(memberPwdRequestDto));
    }

    @PostMapping("/profile/img")
    public ResponseEntity<ResponseDto> updateMemberProfileImg(@ModelAttribute MemberImgRequestDto memberImgRequestDto) throws IOException {
        return ResponseEntity.ok(memberService.updateMemberProfileImg(memberImgRequestDto));
    }

    @PostMapping("/profile")
    public ResponseEntity<ResponseDto> updateMemberProfile(@RequestPart(required = false) @ModelAttribute MemberProfileRequestDto memberProfileRequestDto) throws IOException {
        return ResponseEntity.ok(memberService.updateMemberProfile(memberProfileRequestDto));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ResponseDto> deleteMember() throws IOException {
        // 해당 member가 작성한 board와 그 board에 관련 데이터* 삭제:
        // boardService에 method를 구현하여 memberService와 boardService 간의 이중 주입 방지
        // *관련 데이터: 해당 board에 업로드된 이미지들, 좋아요, 댓글, 대댓글
        boardService.deleteBoardListFromMember();
        return ResponseEntity.ok(memberService.deleteMember());
    }
}
