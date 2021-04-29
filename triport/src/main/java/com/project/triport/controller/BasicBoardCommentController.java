package com.project.triport.controller;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.User;
import com.project.triport.requestDto.BasicBoardCommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BasicBoardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BasicBoardCommentController {

    private final BasicBoardCommentService basicBoardCommentService;

    // BasicBoard Comment 작성
    @PostMapping("/api/boards/basic/comments/{basicId}")
    public ResponseDto createBasicBoardComment(@PathVariable Long basicId, @RequestBody BasicBoardCommentRequestDto requestDto, User user) {
        return basicBoardCommentService.createBasicBoardComment(basicId, requestDto, user);
    }

    // BasicBoard Comment 수정
    @PutMapping("/api/boards/basic/comments/{commentId}")
    public ResponseDto updateBasicBoardComment(@PathVariable Long commentId, @RequestBody BasicBoardCommentRequestDto requestDto, User user) {
        return basicBoardCommentService.updateBasicBoardComment(commentId, requestDto, user);
    }

    // BasicBoard Comment 삭제
    @DeleteMapping("/api/boards/basic/comments/{commentId}")
    public ResponseDto deleteBasicBoardComment(@PathVariable Long commentId, User user) {
        return basicBoardCommentService.deleteBasicBoardComment(commentId, user);
    }
}
