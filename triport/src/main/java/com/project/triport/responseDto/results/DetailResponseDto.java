package com.project.triport.responseDto.results;

import com.project.triport.entity.Post;
import com.project.triport.entity.User;
import com.project.triport.responseDto.results.property.AccessUserResponseDto;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import com.project.triport.responseDto.results.property.information.InformationResponseDto;
import com.project.triport.responseDto.results.property.information.PostInformationResponseDto;

import java.util.List;

public class DetailResponseDto {

    private InformationResponseDto information;
    private AuthorResponseDto authorResponseDto;
    private List<CommentResponseDto> commentResponseDtoList;
    private AccessUserResponseDto user;

    public DetailResponseDto(Post post, User accessUser) {
        this.information = new PostInformationResponseDto(post);
        this.authorResponseDto = new AuthorResponseDto(post);
        this.user = new AccessUserResponseDto(post, accessUser);
    }
}
