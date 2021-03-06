package com.project.triport.repository;

import com.project.triport.entity.Board;
import com.project.triport.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = ("member"))
    List<Board> findByMember(Member member);

    @EntityGraph(attributePaths = ("member"))
    Slice<Board> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageable);
}
