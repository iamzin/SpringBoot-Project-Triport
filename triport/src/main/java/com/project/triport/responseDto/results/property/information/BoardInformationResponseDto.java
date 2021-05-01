package com.project.triport.responseDto.results.property.information;

import com.project.triport.entity.Board;

import java.time.format.DateTimeFormatter;

public class BoardInformationResponseDto extends InformationResponseDto {

    // BasicBoard
    private String title;
    private String description;
    private Long commentNum;

    public BoardInformationResponseDto(Board board){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.likeNum = board.getLikeNum();
        this.commentNum = board.getCommentNum();
        this.modifiedAt = board.getModifiedAt().format(formatter);
    }
}
