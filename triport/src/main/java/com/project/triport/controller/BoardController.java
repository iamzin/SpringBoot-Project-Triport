package com.project.triport.controller;

//import com.project.triport.responseDto.MsgResponseDto;

import com.project.triport.requestDto.BoardRequestDto;
import com.project.triport.requestDto.ImageRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.service.BoardService;
import com.project.triport.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final S3ImageService s3ImageService;

    // Trilog 게시글 전체 리스트 조회
    @GetMapping("/api/all/boards")
    public ResponseDto getBoardList(@Min(value=1, message = "페이지는 1보다 커야합니다.") @RequestParam int page, @RequestParam String filter, @RequestParam String keyword) {
        return boardService.getBoardList(page, filter, keyword);
    }

    // 게시글 상세 조회
    @GetMapping("/api/all/boards/detail/{boardId}")
    public ResponseDto getBoardDetail(@PathVariable Long boardId) {
        return boardService.getBoardDetail(boardId);
    }

    // 로그인한 Member가 작성한 전체 게시글 리스트 조회
    @GetMapping("/api/boards/member")
    public ResponseDto getBoardListFromMember() {
        return boardService.getBoardListCreatedByMember();
    }


    // 로그인한 Member가 좋아요 누른 게시글 리스트 조회
    @GetMapping("/api/boards/member/like")
    public ResponseDto getBoardListMemberLiked() {
        return boardService.getBoardListMemberLiked();
    }


    // 게시글 작성
    @PostMapping("/api/boards")
    public ResponseDto createBoard(@RequestBody @Valid BoardRequestDto requestDto) throws IOException {
        return boardService.createBoard(requestDto);
    }

    // 게시글 수정
    @PutMapping("/api/boards/{boardId}")
    public ResponseDto updateBoard(@PathVariable Long boardId, @RequestBody BoardRequestDto requestDto) throws IOException {
        return boardService.updateBoard(boardId, requestDto);
    }

    // 게시글 삭제
    @DeleteMapping("/api/boards/{boardId}")
    public ResponseDto deleteBoard(@PathVariable Long boardId) throws IOException {
        return boardService.deleteBoard(boardId);
    }

    // 신규 게시글 작성 시 이미지 업로드
    @PostMapping("/api/boards/image")
    public ResponseDto uploadImageFromBoard(@ModelAttribute ImageRequestDto requestDto) throws IOException {
        // 리턴값: 클라우드 프론트 이미지 url
        return s3ImageService.uploadImageToNewBoard(requestDto);
    }

    // 기존 게시글 수정 시 이미지 업로드
    @PostMapping("/api/boards/image/{boardId}")
    public ResponseDto uploadImageFromUpdatingBoard(@PathVariable Long boardId, MultipartFile imageFile) throws IOException {
        return s3ImageService.uploadImageToUpdatingBoard(boardId, imageFile);
    }
}
