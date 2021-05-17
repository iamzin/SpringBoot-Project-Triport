package com.project.triport.repository;

import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentParent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentChildRepository extends JpaRepository<CommentChild, Long> {

    @EntityGraph(attributePaths = ("member"))
    Slice<CommentChild> findByCommentParent(CommentParent commentParent, Pageable pageable);

}
