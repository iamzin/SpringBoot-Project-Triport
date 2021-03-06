package com.project.triport.repository;

import com.project.triport.entity.Board;
import com.project.triport.entity.CommentParent;
import com.project.triport.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentParentRepository extends JpaRepository<CommentParent, Long> {

    @EntityGraph(attributePaths = ("member"))
    Slice<CommentParent> findByBoard(Board board, Pageable pageable);

    @EntityGraph(attributePaths = ("board"))
    List<CommentParent> findAllByMember(Member member);
    void deleteAllByMember(Member member);
}
