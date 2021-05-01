package com.project.triport.responseDto.results.property;

import com.project.triport.entity.BasicBoardComment;
import com.project.triport.entity.PostComment;

public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String profileImgUrl;
    private String contents;
    // 밑에 지우고 리스트에 추가해야됨
    private Boolean last;
    private Long totalPage;

    public CommentResponseDto(PostComment postComment){
        this.id = postComment.getId();
        this.nickname = postComment.getUser().getNickname();
        this.profileImgUrl = postComment.getUser().getProfileImgUrl();
        this.contents = postComment.getContents();
    }

    public CommentResponseDto(BasicBoardComment basicBoardComment){
        this.id = basicBoardComment.getId();
        this.nickname = basicBoardComment.getMember().getEmail(); //수정사항
        this.profileImgUrl = basicBoardComment.getMember().getEmail(); // 수정사항
        this.contents = basicBoardComment.getContents();
    }

    public CommentResponseDto(BasicBoardComment basicBoardComment, Boolean last, Long totalPage){
        this.id = basicBoardComment.getId();
        this.nickname = basicBoardComment.getMember().getEmail(); //수정사항
        this.profileImgUrl = basicBoardComment.getMember().getEmail(); // 수정사항
        this.contents = basicBoardComment.getContents();
        this.last = last;
        this.totalPage = totalPage;
    }
}
