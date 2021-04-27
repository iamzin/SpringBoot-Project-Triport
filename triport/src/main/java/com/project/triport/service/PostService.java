package com.project.triport.service;

import com.project.triport.entity.Post;
import com.project.triport.repository.PostRepository;
import com.project.triport.responseDto.ObjectResponseDto;
import com.project.triport.responseDto.PostListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostListResponseDto readPostsAll(){
        List<Post> Posts = postRepository.findAll();

        return;
    }
}
