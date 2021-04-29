package com.project.triport.repository;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardLike;
import com.project.triport.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicBoardLikeRepository extends JpaRepository<BasicBoardLike, Long> {
    boolean existsByBasicBoardAndUser(BasicBoard basicBoard, User user);
    void deleteByBasicBoardAndUser(BasicBoard basicBoard, User user);
}
