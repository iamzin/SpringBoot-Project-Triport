package com.project.triport.responseDto.results;

import com.project.triport.entity.Board;
import com.project.triport.entity.Post;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.information.BoardInformationResponseDto;
import com.project.triport.responseDto.results.property.information.InformationResponseDto;
import com.project.triport.responseDto.results.property.information.PostInformationResponseDto;
import com.project.triport.responseDto.results.property.AccessUserResponseDto;
import lombok.Getter;

@Getter
public class ListResponseDto {
    private InformationResponseDto information;
    private AuthorResponseDto author;
    private AccessUserResponseDto member;

    public ListResponseDto(Post post, Boolean isLike){
        this.information = new PostInformationResponseDto(post);
        this.author = new AuthorResponseDto(post);
        this.member = new AccessUserResponseDto(isLike);
    }

    public ListResponseDto(Board board, Boolean isLike){
        this.information = new BoardInformationResponseDto(board);
        this.author = new AuthorResponseDto(board);
        this.member = new AccessUserResponseDto(isLike);
    }
}

