package com.project.triport.controller;

import com.project.triport.entity.Post;
import com.project.triport.entity.PostComment;
import com.project.triport.entity.User;
import com.project.triport.requestDto.PostCommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/api/posts/comments/{postId}")
    public ResponseDto createComment(@PathVariable Long postId, @RequestBody PostCommentRequestDto requestDto, User user){
        return postCommentService.createComment(postId, requestDto, user);
    }

    @PutMapping("/api/posts/comments/{commentId}")
    public ResponseDto updateComment(){return postCommentService.updateComment();}

    @DeleteMapping("/api/posts/comments/{commentId}")
    public ResponseDto deleteComment(){return postCommentService.deleteComment();}

}
