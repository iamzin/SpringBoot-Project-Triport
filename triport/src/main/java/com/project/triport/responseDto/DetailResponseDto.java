package com.project.triport.responseDto;

import lombok.Getter;

@Getter
public class DetailResponseDto {
    private Boolean ok;
    private Object results;
    private String msg;

//    public DetailResponseDto(Boolean ok, Object results, String msg){
//        this.ok = ok;
//        this.results = results;
//        this.msg = msg;
//    }
}
