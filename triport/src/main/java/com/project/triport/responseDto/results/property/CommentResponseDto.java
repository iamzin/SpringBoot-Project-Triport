package com.project.triport.responseDto.results.property;

import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentParent;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String profileImgUrl;
    private String contents;
    private Long likeNum;
    private String modifiedAt;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

    public CommentResponseDto(CommentParent commentParent){
        this.id = commentParent.getId();
        this.nickname = commentParent.getMember().getNickname();
        this.profileImgUrl = commentParent.getMember().getProfileImgUrl();
        this.contents = commentParent.getContents();
        this.likeNum = commentParent.getLikeNum();
        this.modifiedAt = commentParent.getModifiedAt().format(formatter);
    }

    public CommentResponseDto(CommentChild commentChild){
        this.id = commentChild.getId();
        this.nickname = commentChild.getMember().getNickname();
        this.profileImgUrl = commentChild.getMember().getProfileImgUrl();
        this.contents = commentChild.getContents();
//        this.likeNum = commentParent.getLikeNum();
        this.modifiedAt = commentChild.getModifiedAt().format(formatter);
    }
}
