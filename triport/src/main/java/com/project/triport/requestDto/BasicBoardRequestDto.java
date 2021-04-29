package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasicBoardRequestDto {
    private String title;
    private String description;
    private String imgUrl;
    private String videoUrl;
    private String address;
}
