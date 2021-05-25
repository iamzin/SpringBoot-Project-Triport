package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class MemberNicknameRequestDto {
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 3, max = 12, message = "닉네임은 3-12자리 이내로 입력해 주세요.")
    @Pattern(regexp = "^[가-힣|a-zA-Z0-9]*${3,12}",
            message = "닉네임은 한글, 영문, 숫자, 특수문자(._)만 3-12자리 이내로 입력할 수 있습니다.")
    private String nickname;
}
