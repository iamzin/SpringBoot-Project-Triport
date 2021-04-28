package com.project.triport.repository;

import com.project.triport.entity.Post;
import com.project.triport.entity.PostLike;
import com.project.triport.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndUser(Post post, User user);
}
