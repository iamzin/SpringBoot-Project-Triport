package com.project.triport.responseDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ResponseDto { // 10개씩 내보내야 함. page 처리 필요.

    private Boolean ok;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object results;
    private String msg;
    private String subMsg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean last; // 마지막 postId일 때 true, 이외에는 false
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int status;

    public ResponseDto(Boolean ok, Object results, String msg, Boolean last) {
        this.ok = ok;
        this.results = results;
        this.msg = msg;
        this.last = last;
    }

    public ResponseDto(Boolean ok, Object results, String msg, Boolean last, int status) {
        this.ok = ok;
        this.results = results;
        this.msg = msg;
        this.last = last;
        this.status = status;
    }

    public ResponseDto(Boolean ok, Object results, String msg) {
        this.ok = ok;
        this.results = results;
        this.msg = msg;
    }

    public ResponseDto(Boolean ok, Object results, String msg, int status) {
        this.ok = ok;
        this.results = results;
        this.msg = msg;
        this.status = status;
    }

    public ResponseDto(Boolean ok, String msg) {
        this.ok = ok;
        this.msg = msg;
    }

    public ResponseDto(Boolean ok, String msg, int status) {
        this.ok = ok;
        this.msg = msg;
        this.status = status;
    }

    public ResponseDto(Boolean ok, String msg, String subMsg, int status) {
        this.ok = ok;
        this.msg = msg;
        this.subMsg = subMsg;
        this.status = status;
    }
}