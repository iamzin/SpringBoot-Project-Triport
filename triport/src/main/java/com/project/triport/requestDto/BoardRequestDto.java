package com.project.triport.requestDto;

import com.project.triport.responseDto.results.ImageResponseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class BoardRequestDto {
    @NotBlank(message = "제목을 입력해 주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요.")
    private String description;
    @NotBlank(message = "주소를 입력해 주세요.")
    private String address;
    private List<ImageResponseDto> imageUrlList;
}
