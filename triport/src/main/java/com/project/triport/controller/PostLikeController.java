package com.project.triport.controller;


import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/api/posts/like/{postId}")
    public ResponseDto creatDeletePostLike(@PathVariable Long postId,@AuthenticationPrincipal User user){return postLikeService.creatDeletePostLike(postId,user);}
}
