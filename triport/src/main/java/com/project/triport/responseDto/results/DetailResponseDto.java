package com.project.triport.responseDto.results;

import com.project.triport.entity.Board;
import com.project.triport.entity.Post;
import com.project.triport.responseDto.results.property.AccessMemberResponseDto;
import com.project.triport.responseDto.results.property.AuthorResponseDto;
import com.project.triport.responseDto.results.property.information.BoardInformationResponseDto;
import com.project.triport.responseDto.results.property.information.InformationResponseDto;
import com.project.triport.responseDto.results.property.information.PostInformationResponseDto;
import lombok.Getter;

@Getter
public class DetailResponseDto {

    private InformationResponseDto information;
    private AuthorResponseDto author;
    private AccessMemberResponseDto member;

    public DetailResponseDto(Board board, Boolean isLike) {
        this.information = new BoardInformationResponseDto(board, board.getAddress());
        this.author = new AuthorResponseDto(board);
        this.member = new AccessMemberResponseDto(isLike);
    }

    public DetailResponseDto(Post post, Boolean isLike, Boolean isMember) {
        this.information = new PostInformationResponseDto(post);
        this.author = new AuthorResponseDto(post);
        this.member = new AccessMemberResponseDto(isLike, isMember);
    }

    public DetailResponseDto(Board board, Boolean isLike, Boolean isMember) {
        this.information = new BoardInformationResponseDto(board);
        this.author = new AuthorResponseDto(board);
        this.member = new AccessMemberResponseDto(isLike, isMember);
    }
}
