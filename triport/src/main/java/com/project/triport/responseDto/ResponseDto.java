package com.project.triport.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseDto { // 10개씩 내보내야 함. page 처리 필요.

    private Boolean ok;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object results;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean last; // 마지막 postId일 때 true, 이외에는 false

    public ResponseDto(Boolean ok, List<Object> results, String msg, Boolean last){
        this.ok = ok;
        this.results = results;
        this.msg = msg;
        this.last = last;
    }

    public ResponseDto(Boolean ok, Object results, String msg){
        this.ok = ok;
        this.results = results;
        this.msg = msg;
    }

    public ResponseDto(Boolean ok, String msg){
        this.ok = ok;
        this.msg = msg;
    }
}