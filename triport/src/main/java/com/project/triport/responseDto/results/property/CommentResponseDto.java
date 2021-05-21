package com.project.triport.responseDto.results.property;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long commentChildNum;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cratedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String modifiedAt;

    public CommentResponseDto(CommentParent commentParent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
        this.id = commentParent.getId();
        this.nickname = commentParent.getMember().getNickname();
        this.profileImgUrl = commentParent.getMember().getProfileImgUrl();
        this.contents = commentParent.getContents();
        this.likeNum = commentParent.getLikeNum();
        this.commentChildNum = commentParent.getCommentChildNum();
        this.cratedAt = commentParent.getCreatedAt().format(formatter);
    }

    public CommentResponseDto(CommentChild commentChild) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
        this.id = commentChild.getId();
        this.nickname = commentChild.getMember().getNickname();
        this.profileImgUrl = commentChild.getMember().getProfileImgUrl();
        this.contents = commentChild.getContents();
        this.likeNum = commentChild.getLikeNum();
        this.modifiedAt = commentChild.getModifiedAt().format(formatter);
    }
}
