package com.project.triport.service;

import com.project.triport.responseDto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class PostCommentService {
    public ResponseDto createComment(){

        return new ResponseDto(true,"댓글 작성 완료!");
    }

    public ResponseDto updateComment(){return new ResponseDto(true,"댓글 수정 완료!");}

    public ResponseDto deleteComment(){return new ResponseDto(true,"댓글을 삭제 하였습니다.");}
}
