package com.project.triport.repository;

import com.project.triport.entity.Member;
import com.project.triport.entity.Post;
import com.project.triport.entity.PostLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findAllByMember(Member member);

    boolean existsByPostAndMember(Post post, Member member);

    void deleteByPostAndMember(Post post, Member member);

    @EntityGraph(attributePaths = ("post"))
    List<PostLike> findByMember(Member member);
    void deleteByPost(Post post);
    void deleteAllByMember(Member member);
}
