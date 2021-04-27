package com.project.triport.controller;

import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.responseDto.DetailResponseDto;
import com.project.triport.responseDto.ListResponseDto;
import com.project.triport.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")
    public ListResponseDto readPostsAll(){return postService.readPostsAll();}

    @GetMapping("/api/posts/detail")
    public DetailResponseDto readPost(){return postService.readPost();}

    @GetMapping("/api/posts/user")
    public ListResponseDto readPostsUser(){return postService.readPostsUser();}

    // post 사진 업로드 부분 api 별도 필요한지 확인 필요

    @PostMapping("/api/posts")
    public MsgResponseDto createPost(){return postService.createPost();}

    @PutMapping("/api/posts/{postId}")
    public MsgResponseDto updatePost(){return postService.updatePost();}

    @DeleteMapping("/api/posts/{postId}")
    public MsgResponseDto deletePost(){return postService.deletePost();}

}
