package com.project.triport.repository;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardLike;
import com.project.triport.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    boolean existsByBoardAndMember(Board board, Member member);

    void deleteByBoardAndMember(Board board, Member member);

    List<BoardLike> findByMember(Member member);
}
