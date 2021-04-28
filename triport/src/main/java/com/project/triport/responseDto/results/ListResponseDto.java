package com.project.triport.responseDto.results;

import com.project.triport.entity.Post;
import com.project.triport.entity.User;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.information.InformationResponseDto;
import com.project.triport.responseDto.results.property.information.PostInformationResponseDto;
import com.project.triport.responseDto.results.property.AccessUserResponseDto;

public class ListResponseDto {
    private InformationResponseDto information;
    private AuthorResponseDto authorResponseDto;
    private AccessUserResponseDto user;

    public ListResponseDto(Post post, User accessUser){
        this.information = new PostInformationResponseDto(post);
        this.authorResponseDto = new AuthorResponseDto(post);
        this.user = new AccessUserResponseDto(post, accessUser);
    }



}

