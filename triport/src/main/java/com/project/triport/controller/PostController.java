package com.project.triport.controller;


import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")
    public ResponseDto readPostsAll(
            @RequestParam int page,
            @RequestParam String filter) {
        return postService.readPostsAll(page, filter);
    }

    @GetMapping("/api/posts/detail/{postId}")
    public ResponseDto readPost(@PathVariable Long postId) {
        return postService.readPost(postId);
    }

    @GetMapping("/api/posts/member")
    public ResponseDto readPostsMember() {
        return postService.readPostsMember();
    }

    // post 사진 업로드 부분 api 별도 필요한지 확인 필요
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

}
