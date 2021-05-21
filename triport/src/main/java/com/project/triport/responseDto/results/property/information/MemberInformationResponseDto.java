package com.project.triport.responseDto.results.property.information;

import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInformationResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImgUrl;
    private MemberGrade memberGrade;

    public static MemberInformationResponseDto of(Member member) {
        return new MemberInformationResponseDto(member.getId(), member.getEmail(), member.getNickname(),
                member.getProfileImgUrl(), member.getMemberGrade());
    }
}
