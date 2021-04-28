package com.project.triport.responseDto.results.property.information;

import com.project.triport.entity.Post;

import java.time.format.DateTimeFormatter;

public class PostInformationResponseDto extends InformationResponseDto {

    public PostInformationResponseDto(Post post){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = post.getId();
        this.description = post.getDescription();
        this.imgUrl = post.getImgUrl();
        this.likeNum = post.getLikeNum();
        this.commentNum = post.getCommentNum();
        this.modifiedAt = post.getModifiedAt().format(formatter);
    }
}
