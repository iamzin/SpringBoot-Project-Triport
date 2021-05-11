package com.project.triport.responseDto.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentParent;
import com.project.triport.responseDto.results.property.AccessMemberResponseDto;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.Getter;

@Getter
public class CommentListResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CommentResponseDto commentParent;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CommentResponseDto commentChild;
    private AuthorResponseDto author;
    private AccessMemberResponseDto user;

    public CommentListResponseDto(CommentParent commentParent, Boolean isLike) {
        this.commentParent = new CommentResponseDto(commentParent);
        this.author = new AuthorResponseDto(commentParent);
        this.user = new AccessMemberResponseDto(isLike);
    }

    public CommentListResponseDto(CommentChild commentChild, Boolean isLike) {
        this.commentChild = new CommentResponseDto(commentChild);
        this.author = new AuthorResponseDto(commentChild);
        this.user = new AccessMemberResponseDto(isLike);
    }
}
