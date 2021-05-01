package com.project.triport.responseDto.results.property;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.Post;

public class AuthorResponseDto {
    // Author 관련 전달 사항
    private String nickname; // email 주소(id)
    private String profileImgUrl; // http://15.165.205.40/profiles/img1.png

    public AuthorResponseDto(Post post){
        this.nickname = post.getUser().getNickname();
        this.profileImgUrl = post.getUser().getProfileImgUrl();
    }

    public AuthorResponseDto(BasicBoard basicBoard){
        this.nickname = basicBoard.getMember().getEmail(); //nickname
        this.profileImgUrl = basicBoard.getMember().getEmail(); //profileImgUrl
    }
}
