package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    private String description;
    private String imgURL;
    private Long likeNum;
    private Long commentNum;
}
