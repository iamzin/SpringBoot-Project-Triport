package com.project.triport.responseDto;

import com.project.triport.entity.PostComment;
import lombok.Getter;

@Getter
public class PostCommentResponseDto {
    private Long commentId;
    private String nickname;
    private String profileImgUrl;
    private String commentContents;


    public PostCommentResponseDto(PostComment postComment){
        this.commentId = postComment.getId();
        this.nickname = postComment.getUser().getNickname();
        this.profileImgUrl = postComment.getUser().getProfileImgUrl();
        this.commentContents = postComment.getCommentContents();
    }

}
