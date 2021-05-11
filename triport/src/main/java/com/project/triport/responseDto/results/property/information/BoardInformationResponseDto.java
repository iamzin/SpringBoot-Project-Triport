package com.project.triport.responseDto.results.property.information;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.triport.entity.Board;
import com.project.triport.entity.BoardImageInfo;
import com.project.triport.service.S3ImageService;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class BoardInformationResponseDto extends InformationResponseDto {

    // BasicBoard
    private String title;
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String thumbNailUrl;
    private Long commentNum;

    public BoardInformationResponseDto(Board board){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();

        if(board.getBoardImageInfoList().size() > 0) {
            this.thumbNailUrl = "https://" + S3ImageService.CLOUD_FRONT_DOMAIN_NAME + "/" + board.getBoardImageInfoList().get(0).getFilePath();
        } else {
            this.thumbNailUrl = "";
        }

        this.likeNum = board.getLikeNum();
        this.commentNum = board.getCommentNum();
        this.modifiedAt = board.getModifiedAt().format(formatter);
    }

    public BoardInformationResponseDto(Board board, String address){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.address = address;

        this.likeNum = board.getLikeNum();
        this.commentNum = board.getCommentNum();
        this.modifiedAt = board.getModifiedAt().format(formatter);
    }


}
