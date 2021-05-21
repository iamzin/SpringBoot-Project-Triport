package com.project.triport.controller;

import com.project.triport.requestDto.CommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.CommentParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequiredArgsConstructor
public class CommentParentController {

    private final CommentParentService commentParentService;

    // Comment Parent 리스트 조회(페이징 처리)
    @GetMapping("/api/all/boards/comments/parents/{boardId}")
    public ResponseDto getBoardCommentList(@PathVariable Long boardId, @Min(value=1, message = "페이지는 1보다 커야합니다.") @RequestParam int page) {
        return commentParentService.getPagedCommentParentList(boardId, page);
    }

    // Comment Parent 작성
    @PostMapping("/api/boards/comments/parents/{boardId}")
    public ResponseDto createBoardComment(@PathVariable Long boardId, @RequestBody CommentRequestDto requestDto) {
        return commentParentService.createCommentParent(boardId, requestDto);
    }

    // Comment Parent 수정
    @PutMapping("/api/boards/comments/parents/{commentParentId}")
    public ResponseDto updateBoardComment(@PathVariable Long commentParentId, @RequestBody CommentRequestDto requestDto) {
        return commentParentService.updateCommentParent(commentParentId, requestDto);
    }

    // Comment Parent 삭제
    @DeleteMapping("/api/boards/comments/parents/{commentParentId}")
    public ResponseDto deleteBoardComment(@PathVariable Long commentParentId) {
        return commentParentService.deleteCommentParent(commentParentId);
    }
}
