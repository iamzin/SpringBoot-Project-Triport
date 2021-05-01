package com.project.triport.responseDto.results;

import com.project.triport.entity.Board;
import com.project.triport.entity.Post;
import com.project.triport.responseDto.results.property.AccessUserResponseDto;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import com.project.triport.responseDto.results.property.information.BoardInformationResponseDto;
import com.project.triport.responseDto.results.property.information.InformationResponseDto;
import com.project.triport.responseDto.results.property.information.PostInformationResponseDto;

import java.util.List;

public class DetailResponseDto {

    private InformationResponseDto information;
    private AuthorResponseDto author;
    private List<CommentResponseDto> commentList;
    private AccessUserResponseDto user;

    public DetailResponseDto(Post post, List<CommentResponseDto> commentList, Boolean isLike){
        this.information = new PostInformationResponseDto(post);
        this.author = new AuthorResponseDto(post);
        this.commentList = commentList;
        this.user = new AccessUserResponseDto(isLike);
    }

    public DetailResponseDto(Board board, Boolean isLike, List<CommentResponseDto> commentResponseDtoList) {
        this.information = new BoardInformationResponseDto(board);
        this.author = new AuthorResponseDto(board);
        this.user = new AccessUserResponseDto(isLike);
        this.commentList = commentResponseDtoList;
    }
}
