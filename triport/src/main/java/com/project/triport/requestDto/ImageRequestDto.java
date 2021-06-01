package com.project.triport.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class ImageRequestDto {
    private MultipartFile imageFile;
    @NotBlank
    private String sizingLine; //200*800 (px)
}
