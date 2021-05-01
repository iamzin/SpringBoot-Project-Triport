package com.project.triport.responseDto.results.property;

import lombok.Getter;

@Getter
public class AccessUserResponseDto {
    // islike 관련 전달 사항
    private boolean isLike; // 열람하는 member가 좋아요 했으면 true 아니면 false

    public AccessUserResponseDto(boolean isLike) {
        this.isLike = isLike;
    }
}
