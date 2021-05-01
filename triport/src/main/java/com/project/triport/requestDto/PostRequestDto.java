package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    private String videoUrl;
    private List<String> hashtag;
}
