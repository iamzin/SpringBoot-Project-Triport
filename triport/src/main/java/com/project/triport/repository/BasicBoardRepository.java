package com.project.triport.repository;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.Member;
import com.project.triport.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BasicBoardRepository extends JpaRepository<BasicBoard, Long> {

    Slice<BasicBoard> findBy(Pageable pageable);

    List<BasicBoard> findByMember(Member member);
}
