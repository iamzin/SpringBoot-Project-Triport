package com.project.triport.service;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardComment;
import com.project.triport.repository.BasicBoardCommentRepository;
import com.project.triport.repository.BasicBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicBoardCommentService {

    private final BasicBoardRepository basicBoardRepository;
    private final BasicBoardCommentRepository basicBoardCommentRepository;

    //BasicBoard 상세 페이지 전체 Comment 조회
    public List<BasicBoardComment> getBasicBoardCommentList(Long basicBoardId) {
        BasicBoard basicBoard = basicBoardRepository.findById(basicBoardId).orElseThrow(
                () -> new IllegalArgumentException("게시판 댓글 정보 없음")
        );
        return basicBoardCommentRepository.findByBasicBoard(basicBoard);
    }
}
