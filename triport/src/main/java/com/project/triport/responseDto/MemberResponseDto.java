package com.project.triport.responseDto;

import com.project.triport.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private String email;
    private String nickname;
    private String profileImgUrl;
    private String grade;

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getEmail(), member.getNickname(),
                member.getProfileImgUrl(), member.getGrade());
    }
}
