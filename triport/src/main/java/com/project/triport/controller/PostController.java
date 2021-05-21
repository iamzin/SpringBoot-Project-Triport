package com.project.triport.controller;


import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.requestDto.VideoUrlDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/all/posts")
    public ResponseDto readPostsAll(
            @RequestParam int page,
            @RequestParam String filter,
            @RequestParam String keyword) {
        return postService.readPostsAll(page, filter, keyword);
    }

    @GetMapping("/api/all/posts/detail/{postId}")
    public ResponseDto readPost(@PathVariable Long postId) {
        return postService.readPost(postId);
    }

    @GetMapping("/api/posts/member")
    public ResponseDto readPostsMember() {
        return postService.readPostsMember();
    }

    @GetMapping("/api/posts/member/like")
    public ResponseDto readPostsMemberLike() {
        return postService.readPostsMemberLike();
    }

    @PostMapping("/api/posts")
    public ResponseDto createPost(@ModelAttribute PostRequestDto requestDto) throws IOException {
        return postService.createPost(requestDto);
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseDto updatePost(@PathVariable Long postId,@RequestBody PostRequestDto requestDto) {
        return postService.updatePost(postId, requestDto);
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseDto deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }


    @PostMapping("/api/encoding/posts/video")
    @CrossOrigin(origins = "${server.ipAddress}")
    public void updateUrl(@RequestBody VideoUrlDto requestDto){
            postService.updateUrl(requestDto);
    }
}
