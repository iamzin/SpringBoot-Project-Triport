package com.project.triport.controller;

//import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BasicBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BasicBoardController {

    private final BasicBoardService basicBoardService;

    // Basic 게시글 전체 리스트 조회
    @GetMapping("/api/boards/basic")
    public ResponseDto getBoardBasicList() {
        return basicBoardService.getBasicBoardList();
    }

    // 게시글 상세 조회
    @GetMapping("/api/boards/basic/detail/{id}")
    public ResponseDto getBoardBasicDetail(@PathVariable Long id) {
        return basicBoardService.getBasicBoardDetail(id);
    }

//    @GetMapping("/api/boards/basic/user")

//    @PostMapping("/api/board/basic")
//    public MsgResponseDto createBasicBoard(@RequestBody BasicBoardRequestDto requestDto) {
//        return basicBoardService.createBasicBoard(requestDto);
//    }

//    @PostMapping("/api/boards/basic/photo")
//    @PostMapping("/api/boards/basic/video")


//    @PutMapping("/api/boards/basic/${board_id}")
//    @DeleteMapping("/api/boards/basic/${board_id}")
}
