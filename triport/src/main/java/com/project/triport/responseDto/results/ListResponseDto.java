package com.project.triport.responseDto.results;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.Post;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.information.BasicBoardInformationResponseDto;
import com.project.triport.responseDto.results.property.information.InformationResponseDto;
import com.project.triport.responseDto.results.property.information.PostInformationResponseDto;
import com.project.triport.responseDto.results.property.AccessUserResponseDto;
import lombok.Getter;

@Getter
public class ListResponseDto {
    private InformationResponseDto information;
    private AuthorResponseDto author;
    private AccessUserResponseDto member;

    public ListResponseDto(Post post, boolean isLike){
        this.information = new PostInformationResponseDto(post);
        this.author = new AuthorResponseDto(post);
        this.member = new AccessUserResponseDto(isLike);
    }

    public ListResponseDto(BasicBoard basicBoard, boolean isLike){
        this.information = new BasicBoardInformationResponseDto(basicBoard);
        this.author = new AuthorResponseDto(basicBoard);
        this.member = new AccessUserResponseDto(isLike);
    }
}

