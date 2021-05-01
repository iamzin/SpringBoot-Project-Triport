package com.project.triport.controller;

//import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.entity.Member;
import com.project.triport.entity.User;
import com.project.triport.requestDto.BasicBoardRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BasicBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BasicBoardController {

    private final BasicBoardService basicBoardService;

    // Basic 게시글 전체 리스트 조회
    @GetMapping("/api/boards/basic")
    public ResponseDto getBasicBoardList(@RequestParam int page, @RequestParam String filter) {
        return basicBoardService.getBasicBoardList(page, filter);
    }

    // 게시글 상세 조회
    @GetMapping("/api/boards/basic/detail/{basicId}")
    public ResponseDto getBasicBoardDetail(@PathVariable Long basicId) {
        return basicBoardService.getBasicBoardDetail(basicId);
    }

    // 로그인한 User가 작성한 전체 게시글 리스트 조회
    @GetMapping("/api/boards/basic/user")
    public ResponseDto getBasicBoardListFromUser() {
        return basicBoardService.getBasicBoardListCreatedByUser();
    }


    // 게시글 작성
    @PostMapping("/api/board/basic")
    public ResponseDto createBasicBoard(@RequestBody BasicBoardRequestDto requestDto) {
        return basicBoardService.createBasicBoard(requestDto);
    }

    // 게시글 수정
    @PutMapping("/api/boards/basic/{basicId}")
    public ResponseDto updateBasicBoard(@PathVariable Long basicId, @RequestBody BasicBoardRequestDto requestDto) {
        return basicBoardService.updateBasicBoard(basicId, requestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/api/boards/basic/{basicId}")
    public ResponseDto deleteBasicBoard(@PathVariable Long basicId) {
        return basicBoardService.deleteBasicBoard(basicId);
    }

    //    @PostMapping("/api/boards/basic/photo")
    //    @PostMapping("/api/boards/basic/video")
}
