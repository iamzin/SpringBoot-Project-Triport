package com.project.triport.responseDto.results.property;

import com.project.triport.entity.BasicBoardComment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String profileImgUrl;
    private String contents;
    // 밑에 지우고 리스트에 추가해야됨
    private Boolean last;
    private Long totalPage;

    public CommentResponseDto(BasicBoardComment basicBoardComment){
        this.id = basicBoardComment.getId();
        this.nickname = basicBoardComment.getMember().getNickname(); //수정사항
        this.profileImgUrl = basicBoardComment.getMember().getProfileImgUrl(); // 수정사항
        this.contents = basicBoardComment.getContents();
    }

    public CommentResponseDto(BasicBoardComment basicBoardComment, Boolean last, Long totalPage){
        this.id = basicBoardComment.getId();
        this.nickname = basicBoardComment.getMember().getNickname(); //수정사항
        this.profileImgUrl = basicBoardComment.getMember().getProfileImgUrl(); // 수정사항
        this.contents = basicBoardComment.getContents();
        this.last = last;
        this.totalPage = totalPage;
    }
}
