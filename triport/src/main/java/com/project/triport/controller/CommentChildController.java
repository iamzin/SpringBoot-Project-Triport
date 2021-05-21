package com.project.triport.controller;

import com.project.triport.requestDto.CommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.CommentChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@Validated
@RestController
@RequiredArgsConstructor
public class CommentChildController {

    private final CommentChildService commentChildService;

    // Comment Child 리스트 조회(페이징 처리)
    @GetMapping("/api/all/boards/comments/children/{commentParentId}")
    public ResponseDto getBoardCommentChildList(@PathVariable Long commentParentId, @Min(value=1, message = "페이지는 1보다 커야합니다.") @RequestParam int page) {
        return commentChildService.getPagedCommentChildList(commentParentId, page);
    }

    // Comment Child 작성
    @PostMapping("/api/boards/comments/children/{commentParentId}")
    public ResponseDto createBoardCommentChild(@PathVariable Long commentParentId, @RequestBody CommentRequestDto requestDto) {
        return commentChildService.createCommentChild(commentParentId, requestDto);
    }

    // Comment Child 수정
    @PutMapping("/api/boards/comments/children/{commentChildId}")
    public ResponseDto updateBoardCommentChild(@PathVariable Long commentChildId, @RequestBody CommentRequestDto requestDto) {
        return commentChildService.updateCommentChild(commentChildId, requestDto);
    }

    // Comment Child 삭제
    @DeleteMapping("/api/boards/comments/children/{commentChildId}")
    public ResponseDto deleteBoardCommentChild(@PathVariable Long commentChildId) {
        return commentChildService.deleteCommentChild(commentChildId);
    }
}
