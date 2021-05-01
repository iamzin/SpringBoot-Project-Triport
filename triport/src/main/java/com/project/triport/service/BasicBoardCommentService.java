package com.project.triport.service;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardComment;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.BasicBoardCommentRepository;
import com.project.triport.repository.BasicBoardRepository;
import com.project.triport.requestDto.BasicBoardCommentRequestDto;
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
public class BasicBoardCommentService {

    private final BasicBoardRepository basicBoardRepository;
    private final BasicBoardCommentRepository basicBoardCommentRepository;

    //BasicBoard 상세 페이지 전체 Comment 조회
    public List<BasicBoardComment> getBasicBoardCommentList(Long basicId) {
        BasicBoard basicBoard = basicBoardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글 정보가 없습니다.")
        );
        return basicBoardCommentRepository.findByBasicBoard(basicBoard);
    }

    //BasicBoard 상세 페이지 전체 Comment 페이징 조회
    public ResponseDto getPagedBasicBoardCommentList(Long basicId, int page) {

        // DB에서 해당 BasicBoard 조회
        BasicBoard basicBoard = basicBoardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        // page 관련 request 설정
        PageRequest pageRequest = PageRequest.of(page-1,5);

        // 페이징 처리된 BasicBoardComment 리스트 조회
        Page<BasicBoardComment> basicBoardCommentPage = basicBoardCommentRepository.findByBasicBoard(basicBoard,pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = basicBoardCommentPage.isLast();

        // 총 페이지 개수
        Long totalPage = (long)basicBoardCommentPage.getTotalPages();

        // Response의 results에 담길 DtoList 객체 생성
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        // 페이징된 BasicBoardComment 리스트를 Dto로 변환
        for (BasicBoardComment basicBoardComment : basicBoardCommentPage) {
            CommentResponseDto responseDto = new CommentResponseDto(basicBoardComment,isLast,totalPage);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList, "해당 Basic 게시글의 댓글 페이징 리스트 조회에 성공하였습니다.");
    }

    // BasicBoard Comment 작성
    @Transactional
    public ResponseDto createBasicBoardComment(Long basicId, BasicBoardCommentRequestDto requestDto) {
        BasicBoard basicBoard = basicBoardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시판 정보가 없습니다.")
        );

        // "user": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        BasicBoardComment basicBoardComment = new BasicBoardComment(requestDto, basicBoard, member);
        basicBoardCommentRepository.save(basicBoardComment);

        basicBoard.updateCommentNum(1);

        return new ResponseDto(true, "댓글 작성이 완료되었습니다.");
    }

    // BasicBoard Comment 수정
    @Transactional
    public ResponseDto updateBasicBoardComment(Long commentId, BasicBoardCommentRequestDto requestDto) {
        BasicBoardComment basicBoardComment = basicBoardCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
        );

        // "user": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        if(member.getId().equals(basicBoardComment.getMember().getId())) {
            basicBoardComment.update(requestDto);
            return new ResponseDto(true, "댓글 수정이 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }

    public ResponseDto deleteBasicBoardComment(Long commentId) {
        BasicBoardComment basicBoardComment = basicBoardCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
        );

        // "user": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        if(member.getId().equals(basicBoardComment.getMember().getId())) {
            basicBoardCommentRepository.deleteById(commentId);
            basicBoardComment.getBasicBoard().updateCommentNum(-1);
            return new ResponseDto(true, "댓글 삭제가 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }
}
