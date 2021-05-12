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
public class Member {

    @Id
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

    public void updatePassword(PasswordEncoder passwordEncoder, MemberRequestDto memberRequestDto) {
        this.password = passwordEncoder.encode(memberRequestDto.toString());
    }

    public void updateTmpPassword(PasswordEncoder passwordEncoder, String tmpPwd) {
        this.password = passwordEncoder.encode(tmpPwd);
    }
}
