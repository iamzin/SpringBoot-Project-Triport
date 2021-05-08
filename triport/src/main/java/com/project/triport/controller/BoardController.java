package com.project.triport.controller;

//import com.project.triport.responseDto.MsgResponseDto;
import com.project.triport.requestDto.BoardRequestDto;
import com.project.triport.requestDto.ImageRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BoardService;
import com.project.triport.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final S3ImageService s3ImageService;

    // Basic 게시글 전체 리스트 조회
    @GetMapping("/api/boards")
    public ResponseDto getBasicBoardList(@RequestParam int page, @RequestParam String filter, @RequestParam String keyword) {
        return boardService.getBoardList(page, filter, keyword);
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
    public ResponseDto createBoard(@RequestBody BoardRequestDto requestDto) throws IOException {
        return boardService.createBoard(requestDto);
    }

    // 게시글 수정
    @PutMapping("/api/boards/{basicId}")
    public ResponseDto updateBoard(@PathVariable Long basicId, @RequestBody BoardRequestDto requestDto) {
        return boardService.updateBoard(basicId, requestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/api/boards/{basicId}")
    public ResponseDto deleteBasicBoard(@PathVariable Long basicId) throws IOException {
        return boardService.deleteBoard(basicId);
    }

    // 게시글 이미지 업로드
    @PostMapping("/api/boards/image")
    public ResponseDto uploadImageFromBoard(@ModelAttribute ImageRequestDto requestDto) throws IOException {
        // 리턴값: 클라우드 프론트 이미지 url
        return s3ImageService.upload(requestDto);
    }
}
