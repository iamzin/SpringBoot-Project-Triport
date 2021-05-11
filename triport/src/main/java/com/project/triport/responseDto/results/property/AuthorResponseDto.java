package com.project.triport.responseDto.results.property;

import com.project.triport.entity.Board;
import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentParent;
import com.project.triport.entity.Post;
import lombok.Getter;

@Getter
public class AuthorResponseDto {
    // Author 관련 전달 사항
    private String nickname; // email 주소(id)
    private String profileImgUrl; // http://15.165.205.40/profiles/img1.png

    public AuthorResponseDto(Post post) {
        this.nickname = post.getMember().getNickname();
        this.profileImgUrl = post.getMember().getProfileImgUrl();
    }

    public AuthorResponseDto(Board board) {
        this.nickname = board.getMember().getNickname(); //nickname
        this.profileImgUrl = board.getMember().getProfileImgUrl(); //profileImgUrl
    }

    public AuthorResponseDto(CommentParent commentParent) {
        this.nickname = commentParent.getMember().getNickname();
        this.profileImgUrl = commentParent.getMember().getProfileImgUrl();
    }

    public AuthorResponseDto(CommentChild commentChild) {
        this.nickname = commentChild.getMember().getNickname();
        this.profileImgUrl = commentChild.getMember().getProfileImgUrl();
    }
}
