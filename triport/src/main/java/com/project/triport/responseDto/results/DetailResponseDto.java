package com.project.triport.responseDto.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.triport.entity.Board;
import com.project.triport.entity.Post;
import com.project.triport.responseDto.results.property.AccessUserResponseDto;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import com.project.triport.responseDto.results.property.information.BoardInformationResponseDto;
import com.project.triport.responseDto.results.property.information.InformationResponseDto;
import com.project.triport.responseDto.results.property.information.PostInformationResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailResponseDto {

    private InformationResponseDto information;
    private AuthorResponseDto author;
    private AccessUserResponseDto member;

    public DetailResponseDto(Post post, Boolean isLike){
        this.information = new PostInformationResponseDto(post);
        this.author = new AuthorResponseDto(post);
        this.member = new AccessUserResponseDto(isLike);
    }

    public DetailResponseDto(Board board, Boolean isLike) {
        this.information = new BoardInformationResponseDto(board);
        this.author = new AuthorResponseDto(board);
        this.member = new AccessUserResponseDto(isLike);
    }
}
