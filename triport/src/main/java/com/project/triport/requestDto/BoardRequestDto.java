package com.project.triport.requestDto;

import com.project.triport.responseDto.results.ImageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardRequestDto {
    private String title;
    private String description;
    private String address;
    private List<ImageResponseDto> imageUrlList;
}
