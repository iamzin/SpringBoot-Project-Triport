package com.project.triport.responseDto.results;

import com.project.triport.service.S3ImageService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageResponseDto {
    private String imageFilePath; // 클라우드 프론트 이미지 url

    public ImageResponseDto(String imageFileName) {
        this.imageFilePath = "https://" + S3ImageService.CLOUD_FRONT_DOMAIN_NAME + "/image/" + imageFileName;
    }
}
