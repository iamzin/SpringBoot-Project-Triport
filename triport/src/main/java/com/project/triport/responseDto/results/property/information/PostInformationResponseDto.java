package com.project.triport.responseDto.results.property.information;

import com.project.triport.entity.Post;
import lombok.Getter;

import java.util.List;

import java.time.format.DateTimeFormatter;

@Getter
public class PostInformationResponseDto extends InformationResponseDto {

    private String videoUrl;
    private List<String> hashtag;

    public PostInformationResponseDto(Post post){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = post.getId();
        this.videoUrl = post.getVideoUrl();
        this.likeNum = post.getLikeNum();
        this.hashtag = post.getHashtag();
        this.modifiedAt = post.getModifiedAt().format(formatter);
    }
}
