package com.project.triport.controller;

import com.project.triport.responseDto.MsgResponseDto;
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
    public MsgResponseDto createComment(){return postCommentService.createComment();}

    @PutMapping("/api/posts/comments/{commentId}")
    public MsgResponseDto updateComment(){return postCommentService.updateComment();}

    @DeleteMapping("/api/posts/comments/{commentId}")
    public MsgResponseDto deleteComment(){return postCommentService.deleteComment();}

}
