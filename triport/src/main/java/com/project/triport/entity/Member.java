package com.project.triport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.triport.requestDto.MemberRequestDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor // @Builder를 쓰기 때문에 필수
@AllArgsConstructor // @Builder를 쓰기 때문에 필수
@Builder
@Table(name = "member")
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberGrade memberGrade;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Authority authority;

    // TODO: password encoding -> Service에서 처리하도록 변경
    public Member toMember(MemberRequestDto memberRequestDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(memberRequestDto.getEmail())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .nickname(memberRequestDto.getNickname())
                .profileImgUrl("https://i.ibb.co/MDKhN7F/kakao-11.jpg")
                .memberGrade(MemberGrade.TRAVELER)
                .authority(Authority.ROLE_USER)
                .build();
    }

    public Member KakaoLoginMember(Long kakaoId, String email, String password, String nickname, String profileImgUrl) {
        return Member.builder()
                .kakaoId(kakaoId)
                .email(email)
                .password(password)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .memberGrade(MemberGrade.TRAVELER)
                .authority(Authority.ROLE_USER)
                .build();
    }

    public void updateKakoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

    public void updateMember(MemberRequestDto memberRequestDto, PasswordEncoder passwordEncoder, String filePath) {
        this.password = passwordEncoder.encode(memberRequestDto.getNewPassword());
        this.nickname = memberRequestDto.getNickname();
        this.profileImgUrl = filePath;
    }

    public void updateTmpPassword(String tmpPwd, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(tmpPwd);
    }
}
