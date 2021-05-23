package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
public class MemberProfileInfoRequestDto {
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 3, max = 12, message = "닉네임은 3-12자리 이내로 입력해 주세요.")
    @Pattern(regexp = "^[가-힣|a-zA-Z0-9]*${3,12}",
            message = "닉네임은 한글, 영문, 숫자, 특수문자(._)만 3-12자리 이내로 입력할 수 있습니다.")
    private String nickname;

    @Size(min = 8, max = 20, message = "비밀번호는 8-20자 이내로 입력해 주세요.")
    @Pattern(regexp = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#*]).*$",
            message = "비밀번호는 영문자와 숫자, 특수문자(!@#*)가 적어도 1개 이상 포함되도록 8-20자리 이내로 입력해 주세요.")
    private String newPassword;

    @Size(min = 8, max = 20, message = "비밀번호는 8-20자 이내로 입력해 주세요.")
    @Pattern(regexp = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#*]).*$",
            message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    private String newPasswordCheck;
}
