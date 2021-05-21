package com.project.triport.responseDto.results.property.information;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.triport.entity.Board;
import com.project.triport.entity.BoardImageInfo;
import com.project.triport.responseDto.results.ImageResponseDto;
import com.project.triport.service.S3ImageService;
import lombok.Getter;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ImageResponseDto> imageUrlList;
    private String createdAt;

    public BoardInformationResponseDto(Board board) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.thumbNailUrl = board.getThumbNailUrl();

        this.likeNum = board.getLikeNum();
        this.commentNum = board.getCommentNum();
        this.createdAt = board.getCreatedAt().format(formatter);
//        this.modifiedAt = board.getModifiedAt().format(formatter);
    }

    public BoardInformationResponseDto(Board board, String address){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = board.getId();
        this.title = board.getTitle();
        this.description = board.getDescription();
        this.address = address;

        this.likeNum = board.getLikeNum();
        this.commentNum = board.getCommentNum();
        this.createdAt = board.getCreatedAt().format(formatter);
//        this.modifiedAt = board.getModifiedAt().format(formatter);

        List<BoardImageInfo> boardImageInfoList = board.getBoardImageInfoList();
        this.imageUrlList = new ArrayList<>();
        for (BoardImageInfo boardImageInfo : boardImageInfoList) {
            ImageResponseDto imageResponseDto = new ImageResponseDto(boardImageInfo.getFilePath());
            this.imageUrlList.add(imageResponseDto);
        }
    }


}
