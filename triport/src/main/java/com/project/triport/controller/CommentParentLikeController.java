package com.project.triport.controller;

import com.project.triport.entity.CommentParentLike;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.CommentParentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentParentLikeController {

    private final CommentParentLikeService commentParentLikeService;

    @PostMapping("/api/boards/comments/parents/like/{commentParentId}")
    public ResponseDto CreateAndDeleteCommentParentLike(@PathVariable Long commentParentId) {
        return commentParentLikeService.CreateAndDeleteCommentParentLike(commentParentId);
    }
}
