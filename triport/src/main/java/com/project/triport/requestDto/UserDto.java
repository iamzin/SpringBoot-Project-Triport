package com.project.triport.requestDto;

import lombok.Getter;

import javax.persistence.Column;

@Getter
public class UserDto {
    private String email;
    private String password;
    private String nickname;
    private String profileImgUrl;
    private String role;
}
