package com.project.triport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.triport.requestDto.MemberRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "member")
public class Member {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private String grade;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String email, String password, String nickname, String profileImgUrl, String grade, Authority authority) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.grade = grade;
        this.authority = authority;
    }

    public void update(MemberRequestDto memberRequestDto) {
        this.email = memberRequestDto.getEmail();
        this.nickname = memberRequestDto.getNickname();
        this.profileImgUrl = memberRequestDto.getProfileImgUrl();
        this.grade = memberRequestDto.getGrade();
    }

    public void updatePassword(MemberRequestDto memberRequestDto) {
        this.password = memberRequestDto.getPassword();

    }

}
