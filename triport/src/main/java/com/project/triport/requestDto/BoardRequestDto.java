package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {
    private String title;
    private String description;
    private String imgUrl;
    private String videoUrl;
    private String address;
}
