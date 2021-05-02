package com.project.triport.controller;

import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.CommentChildLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentChildLikeController {

    private final CommentChildLikeService commentChildLikeService;

    @PostMapping("/api/boards/comments/children/like/{commentChildId}")
    public ResponseDto CreateAndDeleteCommentChildLike(@PathVariable Long commentChildId) {
        return commentChildLikeService.CreateAndDeleteCommentChildLike(commentChildId);
    }
}
