package com.project.triport.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;

@Getter
@AllArgsConstructor
public class UserRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String profileImgUrl;
    private String role;
}
