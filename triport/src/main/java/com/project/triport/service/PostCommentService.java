package com.project.triport.service;

import com.project.triport.entity.PostComment;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    public ResponseDto createComment(){

        return new ResponseDto(true,"댓글 작성 완료!");
    }

    public ResponseDto updateComment(){return new ResponseDto(true,"댓글 수정 완료!");}

    public ResponseDto deleteComment(){return new ResponseDto(true,"댓글을 삭제 하였습니다.");}

    public List<CommentResponseDto> makeCommentResponseDtoList(List<PostComment> postCommentList){
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(PostComment postComment : postCommentList) {
            commentResponseDtoList.add(new CommentResponseDto(postComment));
        }
        return commentResponseDtoList;
    }
}
