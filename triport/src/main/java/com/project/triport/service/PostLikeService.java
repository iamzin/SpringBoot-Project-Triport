package com.project.triport.service;

import com.project.triport.repository.PostLikeRepository;
import com.project.triport.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public ResponseDto creatDeletePostLike(){return new ResponseDto(true, "좋아요 완료!");}
}
