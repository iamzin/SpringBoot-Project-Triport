package com.project.triport.controller;

import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    @PostMapping("/api/boards/like/{boardId}")
    public ResponseDto CreateAndDeleteBoardLike(@PathVariable Long boardId) {
        return boardLikeService.CreateAndDeleteBoardLike(boardId);
    }
}
