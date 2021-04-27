package com.project.triport.service;

import com.project.triport.repository.PostRepository;
import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.responseDto.PostDetailResponseDto;
import com.project.triport.responseDto.PostListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostListResponseDto readPostsAll(){
        return new PostListResponseDto();
    }

    public PostDetailResponseDto readPost(){
        return new PostDetailResponseDto();
    }

    public PostListResponseDto readPostsUser(){
        return new PostListResponseDto();
    }

    public MsgResponseDto createPost(){
        return new MsgResponseDto(true, "포스팅 완료!");
    }

    @Transactional
    public MsgResponseDto updatePost(){
        return new MsgResponseDto(true, "포스트 수정 완료!");
    }

    public MsgResponseDto deletePost(){
        return new MsgResponseDto(true, "포스트를 삭제 하였습니다.");}



}
