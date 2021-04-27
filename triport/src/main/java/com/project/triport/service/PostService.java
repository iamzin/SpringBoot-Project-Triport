package com.project.triport.service;

import com.project.triport.repository.PostRepository;
import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.responseDto.DetailResponseDto;
import com.project.triport.responseDto.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public ListResponseDto readPostsAll(){
        return new ListResponseDto(true,new ArrayList<>(),"무야호",true);
    }

    public DetailResponseDto readPost(){
        return new DetailResponseDto();
    }

    public ListResponseDto readPostsUser(){
        return new ListResponseDto(true,3,"last 없이");
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
