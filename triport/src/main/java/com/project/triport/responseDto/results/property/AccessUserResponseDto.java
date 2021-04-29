package com.project.triport.responseDto.results.property;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.Post;
import com.project.triport.entity.User;

public class AccessUserResponseDto {
    // islike 관련 전달 사항
    private Boolean isLike; // 열람하는 user가 좋아요 했으면 true 아니면 false

    public AccessUserResponseDto(Boolean isLike) {
        this.isLike = isLike;
    }
}
