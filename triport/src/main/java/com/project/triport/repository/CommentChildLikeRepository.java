package com.project.triport.repository;

import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentChildLike;
import com.project.triport.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentChildLikeRepository extends JpaRepository<CommentChildLike, Long> {
    boolean existsByCommentChildAndMember(CommentChild commentChild, Member member);

    void deleteByCommentChildAndMember(CommentChild commentChild, Member member);
}
