package com.project.triport.service;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardComment;
import com.project.triport.entity.User;
import com.project.triport.repository.BasicBoardCommentRepository;
import com.project.triport.repository.BasicBoardRepository;
import com.project.triport.requestDto.BasicBoardCommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // BasicBoard Comment 작성
    @Transactional
    public ResponseDto createBasicBoardComment(Long basicId, BasicBoardCommentRequestDto requestDto, User user) {
        BasicBoard basicBoard = basicBoardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시판 정보가 없습니다.")
        );

        BasicBoardComment basicBoardComment = new BasicBoardComment(requestDto, basicBoard, user);
        basicBoardCommentRepository.save(basicBoardComment);

        basicBoard.updateCommentNum(1);

        return new ResponseDto(true, "댓글 작성이 완료되었습니다.");
    }

    // BasicBoard Comment 수정
    @Transactional
    public ResponseDto updateBasicBoardComment(Long commentId, BasicBoardCommentRequestDto requestDto, User user) {
        BasicBoardComment basicBoardComment = basicBoardCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
        );

        // "user": 현재 로그인한 유저 정보 -> 댓글 작성자가 맞는지 검증 필요
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        if(user.getId().equals(basicBoardComment.getUser().getId())) {
            basicBoardComment.update(requestDto);
            return new ResponseDto(true, "댓글 수정이 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }

    public ResponseDto deleteBasicBoardComment(Long commentId, User user) {
        BasicBoardComment basicBoardComment = basicBoardCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
        );

        // "user": 현재 로그인한 유저 정보 -> 댓글 작성자가 맞는지 검증 필요
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        if(user.getId().equals(basicBoardComment.getUser().getId())) {
            basicBoardCommentRepository.deleteById(commentId);
            basicBoardComment.getBasicBoard().updateCommentNum(-1);
            return new ResponseDto(true, "댓글 삭제가 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }
}
