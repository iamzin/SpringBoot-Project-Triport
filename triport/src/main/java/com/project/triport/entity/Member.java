package com.project.triport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.triport.requestDto.MemberRequestDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberGrade memberGrade;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    public void update(MemberRequestDto memberRequestDto) {
        this.email = memberRequestDto.getEmail();
        this.nickname = memberRequestDto.getNickname();
        this.profileImgUrl = memberRequestDto.getProfileImgUrl();
        this.memberGrade = memberRequestDto.getMemberGrade();
    }

    public void updatePassword(MemberRequestDto memberRequestDto) {
        this.password = memberRequestDto.getPassword();

    }

}
