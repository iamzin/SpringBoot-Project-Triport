package com.project.triport.responseDto.results.property;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class AccessMemberResponseDto {
    // islike 관련 전달 사항
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isMembers;
    private Boolean isLike; // 열람하는 member가 좋아요 했으면 true 아니면 false

    public AccessMemberResponseDto(boolean isLike) {
        this.isLike = isLike;
    }

    public AccessMemberResponseDto(boolean isLike, boolean isMembers) {
        this.isLike = isLike;
        this.isMembers = isMembers;
    }
}
