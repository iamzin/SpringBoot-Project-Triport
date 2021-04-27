package com.project.triport.service;

import com.project.triport.entity.Post;
import com.project.triport.repository.PostRepository;
import com.project.triport.responseDto.PostListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostListResponseDto readPostsAll(){

        // 전체 Post List를 DB에서 불러온다.
        List<Post> PostList = postRepository.findAll();

        return;
    }
}
