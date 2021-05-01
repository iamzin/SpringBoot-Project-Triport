package com.project.triport.controller;

//import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.requestDto.BoardRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // Basic 게시글 전체 리스트 조회
    @GetMapping("/api/boards")
    public ResponseDto getBasicBoardList(@RequestParam int page, @RequestParam String filter) {
        return boardService.getBoardList(page, filter);
    }

    // 게시글 상세 조회
    @GetMapping("/api/boards/detail/{basicId}")
    public ResponseDto getBasicBoardDetail(@PathVariable Long basicId) {
        return boardService.getBoardDetail(basicId);
    }

    // 로그인한 User가 작성한 전체 게시글 리스트 조회
    @GetMapping("/api/boards/member")
    public ResponseDto getBoardListFromUser() {
        return boardService.getBoardListCreatedByUser();
    }


    // 게시글 작성
    @PostMapping("/api/boards")
    public ResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
    }

    // 게시글 수정
    @PutMapping("/api/boards/{basicId}")
    public ResponseDto updateBoard(@PathVariable Long basicId, @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(basicId, requestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/api/boards/{basicId}")
    public ResponseDto deleteBasicBoard(@PathVariable Long basicId) {
        return boardService.deleteBoard(basicId);
    }

    //    @PostMapping("/api/boards/basic/photo")
    //    @PostMapping("/api/boards/basic/video")
}
