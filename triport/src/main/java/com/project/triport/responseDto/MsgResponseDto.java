package com.project.triport.responseDto;

import lombok.Getter;

@Getter
public class MsgResponseDto {
    private Boolean ok;
    private String msg;

    public MsgResponseDto(Boolean ok, String msg){
        this.ok = ok;
        this.msg = msg;
    }
}
