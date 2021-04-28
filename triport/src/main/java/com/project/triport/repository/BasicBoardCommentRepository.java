package com.project.triport.repository;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasicBoardCommentRepository extends JpaRepository<BasicBoardComment,Long> {

    List<BasicBoardComment> findByBasicBoard(BasicBoard basicBoard);
}
