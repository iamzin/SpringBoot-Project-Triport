package com.project.triport.responseDto.results.property.information;

import com.project.triport.entity.Board;

import java.time.format.DateTimeFormatter;

public class BoardInformationResponseDto extends InformationResponseDto {

    // BasicBoard
    private String videoUrl; // http://s3.soghoshg
    private String address; // 주소

    public BoardInformationResponseDto(Board board){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = board.getId();
        this.description = board.getDescription();
        this.imgUrl = board.getImgUrl();
        this.videoUrl = board.getVideoUrl();
        this.likeNum = board.getLikeNum();
        this.commentNum = board.getCommentNum();
        this.address = board.getAddress();
        this.modifiedAt = board.getModifiedAt().format(formatter);
    }
}
