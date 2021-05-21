package com.project.triport.requestDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentRequestDto {
    @NotBlank(message = "댓글 내용을 입력해 주세요.")
    private String contents;
}
