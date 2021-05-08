package com.project.triport.repository;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImageInfoRepository extends JpaRepository<BoardImageInfo, Long> {
    List<BoardImageInfo> findByTempId(String tempId);

    List<BoardImageInfo> findByBoard(Board board);

    List<BoardImageInfo> findByTempIdEqualsAndShouldBeDeletedEquals(String tempId, Boolean shouldBeDeleted);
    List<BoardImageInfo> deleteByTempIdEqualsAndShouldBeDeletedEquals(String tempId, Boolean shouldBeDeleted);
}
