package com.project.triport.requestDto;

import com.project.triport.entity.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor // 모든 값들을 파라미터로 가지는 생성자 만들어줌
//@NoArgsConstructor // 기본 생성자 만들어줌
public class MemberInfoRequestDto {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 주소를 입력해 주세요.")
    private String email;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 3, max = 12, message = "닉네임은 3-12자리 이내로 입력해 주세요.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8-20자 이내로 입력해 주세요.")
    @Pattern(regexp = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#*]).*$",
            message = "비밀번호는 영문자와 숫자, 특수문자(!@#*)가 적어도 1개 이상 포함되도록 8-20자리 이내로 입력해 주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 다시 한 번 입력해 주세요.")
    private String passwordCheck;

    private MultipartFile profileImgFile;
    private MemberGrade memberGrade;

}