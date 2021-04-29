package com.project.triport.controller;

//import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.entity.User;
import com.project.triport.requestDto.BasicBoardRequestDto;
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
    public ResponseDto getBasicBoardList(User user, @RequestParam int page, @RequestParam String filter) {
        return basicBoardService.getBasicBoardList(user, page, filter);
    }

    // 게시글 상세 조회
    @GetMapping("/api/boards/basic/detail/{basicId}")
    public ResponseDto getBasicBoardDetail(User user, @PathVariable Long basicId) {
        return basicBoardService.getBasicBoardDetail(user, basicId);
    }

    // 로그인한 User가 작성한 전체 게시글 리스트 조회
    @GetMapping("/api/boards/basic/user")
    public ResponseDto getBasicBoardListFromUser(User user) {
        return basicBoardService.getBasicBoardListCreatedByUser(user);
    }


    // 게시글 작성
    @PostMapping("/api/board/basic")
    public ResponseDto createBasicBoard(User user, @RequestBody BasicBoardRequestDto requestDto) {
        return basicBoardService.createBasicBoard(user, requestDto);
    }

    // 게시글 수정
    @PutMapping("/api/boards/basic/{basicId}")
    public ResponseDto updateBasicBoard(User user, @PathVariable Long basicId, @RequestBody BasicBoardRequestDto requestDto) {
        return basicBoardService.updateBasicBoard(user, basicId, requestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/api/boards/basic/{basicId}")
    public ResponseDto deleteBasicBoard(User user, @PathVariable Long basicId) {
        return basicBoardService.deleteBasicBoard(user, basicId);
    }

    //    @PostMapping("/api/boards/basic/photo")
    //    @PostMapping("/api/boards/basic/video")
}
