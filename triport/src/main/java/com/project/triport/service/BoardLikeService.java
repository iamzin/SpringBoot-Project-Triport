package com.project.triport.service;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardLike;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.BoardLikeRepository;
import com.project.triport.repository.BoardRepository;
import com.project.triport.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseDto CreateAndDeleteBoardLike(Long basicId) {
        Board board = boardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시판 정보가 없습니다.")
        );

        // "member": 현재 로그인한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        boolean isExist = boardLikeRepository.existsByBoardAndMember(board, member);

        if(isExist) {
            boardLikeRepository.deleteByBoardAndMember(board,member);
            board.updateLikeNum(-1);
            return new ResponseDto(true, "Basic 게시글 좋아요 취소");
        } else {
            BoardLike boardLike = new BoardLike(board, member);
            boardLikeRepository.save(boardLike);
            board.updateLikeNum(+1);
            return new ResponseDto(true, "Basic 게시글 좋아요 추가");
        }
    }
}
