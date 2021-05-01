package com.project.triport.repository;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardLike;
import com.project.triport.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicBoardLikeRepository extends JpaRepository<BasicBoardLike, Long> {
    boolean existsByBasicBoardAndMember(BasicBoard basicBoard, Member member);
    void deleteByBasicBoardAndMember(BasicBoard basicBoard, Member member);
}
