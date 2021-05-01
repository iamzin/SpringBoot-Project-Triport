package com.project.triport.responseDto.results.property;

import com.project.triport.entity.BoardCommentParent;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String profileImgUrl;
    private String contents;


    public CommentResponseDto(BoardCommentParent boardCommentParent){
        this.id = boardCommentParent.getId();
        this.nickname = boardCommentParent.getMember().getNickname();
        this.profileImgUrl = boardCommentParent.getMember().getProfileImgUrl();
        this.contents = boardCommentParent.getContents();
    }
}
