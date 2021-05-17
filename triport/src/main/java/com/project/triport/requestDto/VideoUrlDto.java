package com.project.triport.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.triport.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoUrlDto {
    private Long postId;
    private Boolean posPlay;
    private String videoUrl;

    public VideoUrlDto(Post post){
        this.postId = post.getId();
        this.posPlay = post.getPosPlay();
        this.videoUrl = post.getVideoUrl();
    }
}
