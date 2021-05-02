package com.project.triport.responseDto.results;

import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentParent;
import com.project.triport.responseDto.results.property.AccessUserResponseDto;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.Getter;

@Getter
public class CommentListResponseDto {
    private CommentResponseDto commentParent;
    private AuthorResponseDto author;
    private AccessUserResponseDto user;

    public CommentListResponseDto(CommentParent commentParent, Boolean isLike) {
        this.commentParent = new CommentResponseDto(commentParent);
        this.author = new AuthorResponseDto(commentParent);
        this.user = new AccessUserResponseDto(isLike);
    }

    public CommentListResponseDto(CommentChild commentChild, Boolean isLike) {
        this.commentParent = new CommentResponseDto(commentChild);
        this.author = new AuthorResponseDto(commentChild);
        this.user = new AccessUserResponseDto(isLike);
    }
}
