package com.project.triport.service;

import com.project.triport.responseDto.MsgResponseDto;
import org.springframework.stereotype.Service;

@Service
public class PostCommentService {
    public MsgResponseDto createComment(){

        return new MsgResponseDto(true,"댓글 작성 완료!");
    }

    public MsgResponseDto updateComment(){return new MsgResponseDto(true,"댓글 수정 완료!");}

    public MsgResponseDto deleteComment(){return new MsgResponseDto(true,"댓글을 삭제 하였습니다.");}
}
