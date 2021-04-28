package com.project.triport.requestDto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String description;
    private String imgURL;
    private Long likeNum;
    private Long commentNum;
}
