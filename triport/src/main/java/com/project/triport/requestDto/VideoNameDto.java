package com.project.triport.requestDto;

import lombok.Getter;

@Getter
public class VideoNameDto {
    private String fullname;
    private String type;
    private String filename;

    public VideoNameDto(String videoUrl){
        String[] videoUrlStringList = videoUrl.split("/");

        this.fullname = videoUrlStringList[videoUrlStringList.length - 1];

        String[] videoNameStringList = fullname.split("\\.");

        this.type = videoNameStringList[1];
        this.filename = videoNameStringList[0];

    }
}