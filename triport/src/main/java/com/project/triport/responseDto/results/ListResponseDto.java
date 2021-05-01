package com.project.triport.responseDto.results;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.Post;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.information.BasicBoardInformationResponseDto;
import com.project.triport.responseDto.results.property.information.InformationResponseDto;
import com.project.triport.responseDto.results.property.information.PostInformationResponseDto;
import com.project.triport.responseDto.results.property.AccessUserResponseDto;

public class ListResponseDto {
    private InformationResponseDto information;
    private AuthorResponseDto author;
    private AccessUserResponseDto user;

    public ListResponseDto(Post post, Boolean isLike){
        this.information = new PostInformationResponseDto(post);
        this.author = new AuthorResponseDto(post);
        this.user = new AccessUserResponseDto(isLike);
    }

    public ListResponseDto(BasicBoard basicBoard, Boolean isLike){
        this.information = new BasicBoardInformationResponseDto(basicBoard);
        this.author = new AuthorResponseDto(basicBoard);
        this.user = new AccessUserResponseDto(isLike);
    }
}

