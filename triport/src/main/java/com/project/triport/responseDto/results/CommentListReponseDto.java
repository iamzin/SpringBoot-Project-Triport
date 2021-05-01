package com.project.triport.responseDto.results;

import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentListReponseDto {
    private List<CommentResponseDto> commentList;
    private Boolean last;
    private Long totalPage;

    public CommentListReponseDto(List<CommentResponseDto> commentList, Boolean last, Long totalPage) {
        this.commentList = commentList;
        this.last = last;
        this.totalPage = totalPage;
    }
}
