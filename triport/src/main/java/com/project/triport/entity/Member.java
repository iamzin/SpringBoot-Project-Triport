package com.project.triport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.triport.requestDto.MemberInfoRequestDto;
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberGrade memberGrade;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Authority authority;

    // TODO: password encoding -> Service에서 처리하도록 변경
    // TODO: 기본 가입자는 kakaoId를 0으로 저장 -> front도 함께 작업 필요 (프로필 설정 비밀번호 변경, 회원탈퇴에서 사용 중)
    public Member toMember(MemberInfoRequestDto memberInfoRequestDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(memberInfoRequestDto.getEmail())
                .password(passwordEncoder.encode(memberInfoRequestDto.getPassword()))
                .nickname(memberInfoRequestDto.getNickname())
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

    public void updateMemberProfileImg(String fileUrl) {
        this.profileImgUrl = fileUrl;
    }

    public void updateMemberProfileInfo(String nickname, String newPassword) {
        this.nickname = nickname;
        this.password = newPassword;
    }

    public void updateMemberNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateGrade(MemberGrade memberGrade) {
        this.memberGrade = memberGrade;
    }
}
