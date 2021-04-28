package com.project.triport.responseDto;

import com.project.triport.entity.BasicBoardComment;
import com.project.triport.entity.PostComment;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasicBoardCommentResponseDto {

    private Long commentId;
    private String nickname;
    private String profileImgUrl;
    private String contents;


    public BasicBoardCommentResponseDto(BasicBoardComment basicBoardComment){
        this.commentId = basicBoardComment.getId();
        this.nickname = basicBoardComment.getUser().getNickname();
        this.profileImgUrl = basicBoardComment.getUser().getProfileImgUrl();
        this.contents = basicBoardComment.getContents();
    }

}
