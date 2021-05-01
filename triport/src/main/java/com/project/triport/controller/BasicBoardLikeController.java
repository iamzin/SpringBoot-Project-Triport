package com.project.triport.controller;

import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BasicBoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class BasicBoardLikeController {

    private final BasicBoardLikeService basicBoardLikeService;

    @PostMapping("/api/boards/basic/like/{basicId}")
    public ResponseDto CreateAndDeleteBasicBoardLike(@PathVariable Long basicId) {
        return basicBoardLikeService.CreateAndDeleteBasicBoardLike(basicId);
    }
}
