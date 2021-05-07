package com.project.triport.controller;


import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.PostService;
import com.project.triport.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 로그인 불필요 항목
    @GetMapping("/api/search/posts")
    public ResponseDto readPostsAll(
            @RequestParam int page,
            @RequestParam String filter,
            @RequestParam String keyword) {
        return postService.readPostsAll(page, filter, keyword);
    }

    @GetMapping("/api/search/posts/detail/{postId}")
    public ResponseDto readPost(@PathVariable Long postId) {
        return postService.readPost(postId);
    }

    // 로그인 필요 항목
    @GetMapping("/api/posts/member")
    public ResponseDto readPostsMember() {
        return postService.readPostsMember();
    }

    @PostMapping("/api/posts")
    public ResponseDto createPost(@RequestBody PostRequestDto requestDto) {
        return postService.createPost(requestDto);
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseDto updatePost(@RequestBody PostRequestDto requestDto, @PathVariable Long postId) {
        return postService.updatePost(requestDto, postId);
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseDto deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }

    @PostMapping("/api/posts/video")
    public ResponseDto uploadVideo(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        return postService.uploadVideo(file);
    }
}
