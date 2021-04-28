package com.project.triport.responseDto.results;

import com.project.triport.entity.Post;
import com.project.triport.entity.User;
import com.project.triport.responseDto.results.property.Author;
import com.project.triport.responseDto.results.property.information.Information;
import com.project.triport.responseDto.results.property.information.PostInformation;
import com.project.triport.responseDto.results.property.AccessUser;

public class PostResultsDto {
    private Information information;
    private Author author;
    private AccessUser user;

    public PostResultsDto(Post post, User accessUser){
        this.information = new PostInformation(post);
        this.author = new Author(post);
        this.user = new AccessUser(post, accessUser);
    }



}

