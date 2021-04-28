package com.project.triport.service;

import com.project.triport.repository.PostRepository;
import com.project.triport.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public ResponseDto readPostsAll(){

        return new ResponseDto(true,new ArrayList<>(),"무야호",true);
    }

    public ResponseDto readPost(){
        return new ResponseDto(true,new ArrayList<>(),"무야호",true);
    }

    public ResponseDto readPostsUser(){
        return new ResponseDto(true,"last 없이");
    }

    public ResponseDto createPost(){
        return new ResponseDto(true, "포스팅 완료!");
    }

    @Transactional
    public ResponseDto updatePost(){
        return new ResponseDto(true, "포스트 수정 완료!");
    }

    public ResponseDto deletePost(){
        return new ResponseDto(true, "포스트를 삭제 하였습니다.");}



}
