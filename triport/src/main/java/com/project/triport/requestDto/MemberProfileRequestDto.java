package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class MemberProfileRequestDto {
    @Size(max = 12, message = "닉네임은 12자리 이내로 입력해 주세요.")
    @Pattern(regexp = "^[가-힣|a-zA-Z0-9]*${3,12}",
            message = "닉네임은 한글, 영문, 숫자, 특수문자(._)만 3-12자리 이내로 입력할 수 있습니다.")
    private String nickname;
    private MultipartFile profileImgFile;
}
