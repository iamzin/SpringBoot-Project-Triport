package com.project.triport.service;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardComment;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.BoardCommentRepository;
import com.project.triport.repository.BoardRepository;
import com.project.triport.requestDto.BoardCommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    //진행 필요 사항: 댓글 좋아요(islike) 필요요

   private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;

    //BasicBoard 상세 페이지 전체 Comment 조회
    public List<BoardComment> getBoardCommentList(Long basicId) {
        Board board = boardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글 정보가 없습니다.")
        );
        return boardCommentRepository.findByBoard(board);
    }

    //BasicBoard 상세 페이지 전체 Comment 페이징 조회
    public ResponseDto getPagedBoardCommentList(Long basicId, int page) {

        // DB에서 해당 BasicBoard 조회
        Board board = boardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        // page 관련 request 설정
        PageRequest pageRequest = PageRequest.of(page-1,5);

        // 페이징 처리된 BasicBoardComment 리스트 조회
        Page<BoardComment> BoardCommentPage = boardCommentRepository.findByBoard(board,pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = BoardCommentPage.isLast();

        // 총 페이지 개수
        Long totalPage = (long)BoardCommentPage.getTotalPages();

        // Response의 results에 담길 DtoList 객체 생성
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        // 페이징된 BasicBoardComment 리스트를 Dto로 변환
        for (BoardComment boardComment : BoardCommentPage) {
            CommentResponseDto responseDto = new CommentResponseDto(boardComment,isLast,totalPage);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList, "해당 Basic 게시글의 댓글 페이징 리스트 조회에 성공하였습니다.");
    }

    // BasicBoard Comment 작성
    @Transactional
    public ResponseDto createBoardComment(Long basicId, BoardCommentRequestDto requestDto) {
        Board board = boardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시판 정보가 없습니다.")
        );

        // "member": 현재 로그인한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        BoardComment boardComment = new BoardComment(requestDto, board, member);
        boardCommentRepository.save(boardComment);

        board.updateCommentNum(1);

        return new ResponseDto(true, "댓글 작성이 완료되었습니다.");
    }

    // BasicBoard Comment 수정
    @Transactional
    public ResponseDto updateBoardComment(Long commentId, BoardCommentRequestDto requestDto) {
        BoardComment boardComment = boardCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
        );

        // "member": 현재 로그인한 유저 정보 -> 댓글 작성자가 맞는지 검증 필요!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        // 댓글 작성자가 맞는지 검증
        if(member.getId().equals(boardComment.getMember().getId())) {
            boardComment.update(requestDto);
            return new ResponseDto(true, "댓글 수정이 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }

    public ResponseDto deleteBoardComment(Long commentId) {
        BoardComment boardComment = boardCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
        );

        // "member": 현재 로그인한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        // 댓글 작성자가 맞는지 검증
        if(member.getId().equals(boardComment.getMember().getId())) {
            boardCommentRepository.deleteById(commentId);
            boardComment.getBoard().updateCommentNum(-1);
            return new ResponseDto(true, "댓글 삭제가 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }
}
