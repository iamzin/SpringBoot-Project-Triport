package com.project.triport.responseDto.results.property.information;

import com.project.triport.entity.Post;
import lombok.Getter;

import java.util.List;

import java.time.format.DateTimeFormatter;

@Getter
public class PostInformationResponseDto extends InformationResponseDto {

    private String videoType;
    private String videoUrl;
    private Boolean posPlay;
    private List<String> hashtag;

    public PostInformationResponseDto(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = post.getId();
        this.videoType = post.getVideoType();
        this.videoUrl = post.getVideoUrl();
        this.posPlay = post.getPosPlay();
        this.likeNum = post.getLikeNum();
        this.hashtag = post.getHashtag();
        this.modifiedAt = post.getModifiedAt().format(formatter);
    }
}
