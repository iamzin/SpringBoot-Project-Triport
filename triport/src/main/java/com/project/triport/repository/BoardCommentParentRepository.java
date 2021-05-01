package com.project.triport.repository;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardCommentParent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentParentRepository extends JpaRepository<BoardCommentParent,Long> {

    Slice<BoardCommentParent> findByBoard(Board board, Pageable pageable);

    List<BoardCommentParent> findByBoard(Board board);
}
