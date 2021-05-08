package com.project.triport.requestDto;

import com.project.triport.entity.Authority;
import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private Long id;
    private String email;
    private String password;
    private String passwordCheck;
    private String nickname;
    private String profileImgUrl;
    private MemberGrade memberGrade;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .profileImgUrl("profileImgUrl")
                .memberGrade(MemberGrade.TRAVELER)
                .authority(Authority.ROLE_USER)
                .build();
    }
}
