package com.project.triport.service;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardLike;
import com.project.triport.entity.Member;
import com.project.triport.entity.User;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.BasicBoardLikeRepository;
import com.project.triport.repository.BasicBoardRepository;
import com.project.triport.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBoardLikeService {

    private final BasicBoardLikeRepository basicBoardLikeRepository;
    private final BasicBoardRepository basicBoardRepository;

    @Transactional
    public ResponseDto CreateAndDeleteBasicBoardLike(Long basicId) {
        BasicBoard basicBoard = basicBoardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시판 정보가 없습니다.")
        );

        // "user": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        boolean isExist = basicBoardLikeRepository.existsByBasicBoardAndMember(basicBoard, member);

        if(isExist) {
            basicBoardLikeRepository.deleteByBasicBoardAndMember(basicBoard,member);
            basicBoard.updateLikeNum(-1);
            return new ResponseDto(true, "Basic 게시글 좋아요 취소");
        } else {
            BasicBoardLike basicBoardLike = new BasicBoardLike(basicBoard, member);
            basicBoardLikeRepository.save(basicBoardLike);
            basicBoard.updateLikeNum(+1);
            return new ResponseDto(true, "Basic 게시글 좋아요 추가");
        }
    }
}
