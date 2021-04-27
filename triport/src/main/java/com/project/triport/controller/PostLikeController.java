package com.project.triport.controller;


import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/api/posts/like/{postId}")
    public MsgResponseDto creatDeletePostLike(){return postLikeService.creatDeletePostLike();}
}
