package com.project.triport.controller;


import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/api/posts/like/{postId}")
    public ResponseDto creatDeletePostLike(@PathVariable Long postId) throws IOException, MessagingException {
        return postLikeService.creatDeletePostLike(postId);
    }
}
