package com.project.triport.controller;

import com.project.triport.requestDto.BoardCommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BoardCommentParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardCommentParentController {

    private final BoardCommentParentService boardCommentParentService;

    // BasicBoard Comment Parent 리스트 조회(페이징 처리)
    @GetMapping("/api/boards/comments/parents/{basicId}")
    public ResponseDto getBoardCommentList(@PathVariable Long basicId, @RequestParam int page) {
        return boardCommentParentService.getPagedBoardCommentParentList(basicId,page);
    }

    // BasicBoard Comment Parent 작성
    @PostMapping("/api/boards/comments/parents/{basicId}")
    public ResponseDto createBoardComment(@PathVariable Long basicId, @RequestBody BoardCommentRequestDto requestDto) {
        return boardCommentParentService.createBoardCommentParent(basicId, requestDto);
    }

    // BasicBoard Comment Parent 수정
    @PutMapping("/api/boards/comments/parents/{commentParentId}")
    public ResponseDto updateBoardComment(@PathVariable Long commentParentId, @RequestBody BoardCommentRequestDto requestDto) {
        return boardCommentParentService.updateBoardCommentParent(commentParentId, requestDto);
    }

    // BasicBoard Comment Parent 삭제
    @DeleteMapping("/api/boards/comments/parents/{commentParentId}")
    public ResponseDto deleteBoardComment(@PathVariable Long commentParentId) {
        return boardCommentParentService.deleteBoardCommentParent(commentParentId);
    }
}
