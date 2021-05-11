package com.project.triport.repository;

import com.project.triport.entity.Member;
import com.project.triport.entity.Post;
import com.project.triport.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndMember(Post post, Member member);

    void deleteByPostAndMember(Post post, Member member);
}
