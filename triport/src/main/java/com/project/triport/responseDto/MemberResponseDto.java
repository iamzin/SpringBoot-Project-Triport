package com.project.triport.responseDto;

import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGrade;
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
    private MemberGrade memberGrade;

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getEmail(), member.getNickname(),
                member.getProfileImgUrl(), member.getMemberGrade());
    }
}
