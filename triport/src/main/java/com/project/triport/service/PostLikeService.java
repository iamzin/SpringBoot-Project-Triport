package com.project.triport.service;

import com.project.triport.repository.PostLikeRepository;
import com.project.triport.responseDto.MsgResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public MsgResponseDto creatDeletePostLike(){return new MsgResponseDto(true, "좋아요 완료!");}
}
