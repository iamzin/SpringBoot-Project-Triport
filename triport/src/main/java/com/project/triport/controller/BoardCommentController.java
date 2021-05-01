package com.project.triport.controller;

import com.project.triport.requestDto.BoardCommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BoardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    // BasicBoard Comment 리스트 조회(페이징 처리)
    @GetMapping("/api/boards/comments/{basicId}")
    public ResponseDto getBoardCommentList(@PathVariable Long basicId, @RequestParam int page) {
        return boardCommentService.getPagedBoardCommentList(basicId,page);
    }

    // BasicBoard Comment 작성
    @PostMapping("/api/boards/comments/{basicId}")
    public ResponseDto createBoardComment(@PathVariable Long basicId, @RequestBody BoardCommentRequestDto requestDto) {
        return boardCommentService.createBoardComment(basicId, requestDto);
    }

    // BasicBoard Comment 수정
    @PutMapping("/api/boards/comments/{commentId}")
    public ResponseDto updateBoardComment(@PathVariable Long commentId, @RequestBody BoardCommentRequestDto requestDto) {
        return boardCommentService.updateBoardComment(commentId, requestDto);
    }

    // BasicBoard Comment 삭제
    @DeleteMapping("/api/boards/comments/{commentId}")
    public ResponseDto deleteBoardComment(@PathVariable Long commentId) {
        return boardCommentService.deleteBoardComment(commentId);
    }
}
