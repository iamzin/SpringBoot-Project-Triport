package com.project.triport.controller;


import com.project.triport.entity.User;
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
            User user,
            @RequestParam int page,
            @RequestParam String filter){return postService.readPostsAll(user, page, filter);}

    @GetMapping("/api/posts/{postId}")
    public ResponseDto readPost(@PathVariable Long postId, User user){
        return postService.readPost(postId, user);
    }

    @GetMapping("/api/posts/user?page={pageNum}")
    public ResponseDto readPostsUser(User user){return postService.readPostsUser(user);}

    // post 사진 업로드 부분 api 별도 필요한지 확인 필요

    @PostMapping("/api/posts")
    public ResponseDto createPost(@RequestBody PostRequestDto requestDto, User user){
        return postService.createPost(requestDto, user);
    }

    @PutMapping("/api/posts/{postId}")
    public ResponseDto updatePost(@RequestBody PostRequestDto requestDto, @PathVariable Long postId){
        return postService.updatePost(requestDto,postId);
    }

    @DeleteMapping("/api/posts/{postId}")
    public ResponseDto deletePost(@PathVariable Long postId){return postService.deletePost(postId);}

}
