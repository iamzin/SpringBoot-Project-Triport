package com.project.triport.repository;

import com.project.triport.entity.CommentParent;
import com.project.triport.entity.CommentParentLike;
import com.project.triport.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentParentLikeRepository extends JpaRepository<CommentParentLike, Long> {

    boolean existsByCommentParentAndMember(CommentParent commentParent, Member member);

    void deleteByCommentParentAndMember(CommentParent commentParent, Member member);

    @EntityGraph(attributePaths = ("commentParent"))
    List<CommentParentLike> findAllByMember(Member member);
    void deleteAllByMember(Member member);
}

