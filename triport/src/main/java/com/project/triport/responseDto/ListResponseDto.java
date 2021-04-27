package com.project.triport.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
public class ListResponseDto { // 10개씩 내보내야 함. page 처리 필요.

    private Boolean ok;
    private Object results;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean last; // 마지막 postId일 때 true, 이외에는 false

    public ListResponseDto(Boolean ok, List<Object> results, String msg, Boolean last){
        this.ok = ok;
        this.results = results;
        this.msg = msg;
        this.last = last;
    }

    public ListResponseDto(Boolean ok, Object results, String msg){
        this.ok = ok;
        this.results = results;
        this.msg = msg;
    }
}