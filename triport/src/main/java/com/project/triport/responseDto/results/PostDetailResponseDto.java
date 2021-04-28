package com.project.triport.responseDto.results;

import com.project.triport.entity.Post;
import com.project.triport.entity.User;
import com.project.triport.responseDto.results.property.AccessUser;
import com.project.triport.responseDto.results.property.Author;
import com.project.triport.responseDto.results.property.information.PostInformation;

import java.util.List;

public class PostDetailResponseDto {

    private PostInformation information;
    private Author author;
    private List<Comment> commentList;
    private AccessUser user;

    public PostDetailResponseDto(Post post, User accessUser) {
        this.information = new PostInformation(post);
        this.author = new Author(post);
        this.user = new AccessUser(post, accessUser);
    }
}
