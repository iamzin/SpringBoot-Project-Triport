package com.project.triport.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.triport.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoUrlDto {
    private Long postId;
    private String videoUrl;

    public VideoUrlDto(Post post){
        this.postId = post.getId();
        this.videoUrl = post.getVideoUrl();
    }
}
