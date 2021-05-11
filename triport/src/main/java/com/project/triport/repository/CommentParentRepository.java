package com.project.triport.repository;

import com.project.triport.entity.Board;
import com.project.triport.entity.CommentParent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentParentRepository extends JpaRepository<CommentParent, Long> {

    Slice<CommentParent> findByBoard(Board board, Pageable pageable);

    List<CommentParent> findByBoard(Board board);
}
