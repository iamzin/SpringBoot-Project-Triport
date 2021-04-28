package com.project.triport.responseDto.results.property.information;

import com.project.triport.entity.BasicBoard;

import java.time.format.DateTimeFormatter;

public class BasicBoardInformationResponseDto extends InformationResponseDto {

    // BasicBoard
    private String videoUrl; // http://s3.soghoshg
    private String address; // 주소

    public BasicBoardInformationResponseDto(BasicBoard basicBoard){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = basicBoard.getId();
        this.commentNum = basicBoard.getCommentNum();
        this.imgUrl = basicBoard.getImgUrl();
        this.videoUrl = basicBoard.getVideoUrl();
        this.likeNum = basicBoard.getLikeNum();
        this.commentNum = basicBoard.getCommentNum();
        this.address = basicBoard.getAddress();
        this.modifiedAt = basicBoard.getModifiedAt().format(formatter);
    }
}
