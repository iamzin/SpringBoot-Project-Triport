package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class MemberProfileRequestDto {
    private String nickname;
    private MultipartFile profileImgFile;
}
