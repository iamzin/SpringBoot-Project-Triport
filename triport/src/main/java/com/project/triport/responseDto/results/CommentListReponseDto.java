package com.project.triport.responseDto.results;

import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentListReponseDto {
    private List<CommentResponseDto> commentList;
    private Boolean last;

    public CommentListReponseDto(List<CommentResponseDto> commentList, Boolean last) {
        this.commentList = commentList;
        this.last = last;
    }
}