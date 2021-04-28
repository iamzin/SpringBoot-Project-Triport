package com.project.triport.controller;


import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")
    public ResponseDto readPostsAll(){return postService.readPostsAll();}

    @GetMapping("/api/posts/detail")
    public ResponseDto readPost(){return postService.readPost();}

    @GetMapping("/api/posts/user")
    public ResponseDto readPostsUser(){return postService.readPostsUser();}

    // post 사진 업로드 부분 api 별도 필요한지 확인 필요

    @PostMapping("/api/posts")
    public ResponseDto createPost(){return postService.createPost();}

    @PutMapping("/api/posts/{postId}")
    public ResponseDto updatePost(){return postService.updatePost();}

    @DeleteMapping("/api/posts/{postId}")
    public ResponseDto deletePost(){return postService.deletePost();}

}
