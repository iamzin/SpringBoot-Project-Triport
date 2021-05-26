package com.project.triport.repository;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardImageInfo;
import com.project.triport.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardImageInfoRepository extends JpaRepository<BoardImageInfo, Long> {

    List<BoardImageInfo> findByBoard(Board board);

    List<BoardImageInfo> findByMemberAndBoardIsNull(Member member);

    List<BoardImageInfo> findByMemberAndBoard(Member member, Board board);

    Optional<BoardImageInfo> findFirstByBoardOrderByIdAsc(Board board);

    @Query("select i from BoardImageInfo i where i.member = :member and i.board is null and i.shouldBeDeleted = true")
    List<BoardImageInfo> findDeletingImageInfoFromCreate(@Param("member") Member member);

    @Query("select i from BoardImageInfo i where i.member = :member and i.board = :board and i.shouldBeDeleted = true")
    List<BoardImageInfo> findDeletingImageInfoFromUpdate(@Param("member") Member member, @Param("board") Board board);

    @Modifying
    @Query("delete from BoardImageInfo i where i.member = :member and i.board is null and i.shouldBeDeleted = true")
    void bulkDeleteImageInfoFromCreate(@Param("member") Member member);

    @Modifying
    @Query("delete from BoardImageInfo i where i.member = :member and i.board = :board and i.shouldBeDeleted = true")
    void bulkDeleteImageInfoFromUpdate(@Param("member") Member member, @Param("board") Board board);

    List<BoardImageInfo> findAllByMember(Member member);
    void deleteAllByMember(Member member);
}
