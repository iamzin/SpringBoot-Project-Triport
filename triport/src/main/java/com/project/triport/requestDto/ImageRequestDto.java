package com.project.triport.requestDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ImageRequestDto {
    private MultipartFile imageFile;
    private String tempId;
}
