package com.project.triport.controller;

import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/api/posts/comments/{postId}")
    public ResponseDto createComment(){return postCommentService.createComment();}

    @PutMapping("/api/posts/comments/{commentId}")
    public ResponseDto updateComment(){return postCommentService.updateComment();}

    @DeleteMapping("/api/posts/comments/{commentId}")
    public ResponseDto deleteComment(){return postCommentService.deleteComment();}

}
