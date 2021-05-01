package com.project.triport.repository;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment,Long> {

    Page<BoardComment> findByBoard(Board board, Pageable pageable);

    List<BoardComment> findByBoard(Board board);
}
