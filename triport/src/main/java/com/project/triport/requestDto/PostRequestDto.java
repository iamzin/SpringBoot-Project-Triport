package com.project.triport.requestDto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class PostRequestDto {
    private MultipartFile file;
    private List<String> hashtag;
}
