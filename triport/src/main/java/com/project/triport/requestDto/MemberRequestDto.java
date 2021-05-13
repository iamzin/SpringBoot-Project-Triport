package com.project.triport.requestDto;

import com.project.triport.entity.Authority;
import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 주소를 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8-20자 이내로 입력해 주세요.")
    @Pattern(regexp = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#*]).*$",
            message = "비밀번호는 영문자와 숫자, 특수문자(!@#*)가 적어도 1개 이상 포함되도록 8-20자리 이내로 입력해 주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 다시 한 번 입력해 주세요.")
//    @PwdCheck() // custom validator
//    @Size(min = 8, max = 20, message = "비밀번호는 영어와 숫자를 포함하여 8-20자 이내로 입력해 주세요.")
//    @Pattern(regexp = "[a-zA-Z1-9]{8,20}", message = "비밀번호가 일치하지 않습니다.")
    private String passwordCheck;

    // TODO: profile 수정 validation 분리
//    @NotBlank(message = "비밀번호를 입력해 주세요.")
//    @Size(min = 8, max = 20, message = "비밀번호는 8-20자 이내로 입력해 주세요.")
//    @Pattern(regexp = "^.*(?=^.{8,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#*]).*$",
//            message = "비밀번호는 영문자와 숫자, 특수문자(!@#*)가 적어도 1개 이상 포함되도록 8-20자리 이내로 입력해 주세요.")
//    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    private String newPassword;

//    @NotBlank(message = "비밀번호를 다시 한 번 입력해 주세요.")
////    @PwdCheck() // custom validator
////    @Size(min = 8, max = 20, message = "비밀번호는 영어와 숫자를 포함하여 8-20자 이내로 입력해 주세요.")
////    @Pattern(regexp = "[a-zA-Z1-9]{8,20}", message = "비밀번호가 일치하지 않습니다.")
//    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    private String newPasswordCheck;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 3, max = 12, message = "닉네임은 3-12자리 이내로 입력해 주세요.")
    private String nickname;

    private String profileImgUrl;

    private MemberGrade memberGrade;

//    // TODO: password encoding -> Service에서 처리하도록 변경
//    public Member toMember(PasswordEncoder passwordEncoder) {
//        return Member.builder()
//                .email(email)
//                .password(passwordEncoder.encode(password))
//                .nickname(nickname)
//                .profileImgUrl("https://i.ibb.co/MDKhN7F/kakao-11.jpg")
//                .memberGrade(MemberGrade.TRAVELER)
//                .authority(Authority.ROLE_USER)
//                .build();
//    }
}