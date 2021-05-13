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
    Long id;
    String email;
    String nickname;
    String profileImgUrl;
    MemberGrade grade;

    public MemberInformationResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
    }
}
