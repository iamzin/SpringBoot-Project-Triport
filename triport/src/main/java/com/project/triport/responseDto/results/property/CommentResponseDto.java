package com.project.triport.responseDto.results.property;

import com.project.triport.entity.PostComment;

public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String profileImgUrl;
    private String contents;

    public CommentResponseDto(PostComment postComment){
        this.id = postComment.getId();
        this.nickname = postComment.getUser().getNickname();
        this.profileImgUrl = postComment.getUser().getProfileImgUrl();
        this.contents = postComment.getContents();
    }
}
