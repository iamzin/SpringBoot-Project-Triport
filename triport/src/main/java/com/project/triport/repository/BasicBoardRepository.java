package com.project.triport.repository;

import com.project.triport.entity.BasicBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicBoardRepository extends JpaRepository<BasicBoard, Long> {
    BasicBoard findByTitle(String title);
}
