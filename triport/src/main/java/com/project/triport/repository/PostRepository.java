package com.project.triport.repository;

import com.project.triport.entity.Post;
import com.project.triport.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Slice<Post> findAllBy(Pageable pageable);
    List<Post> findByUser(User user);
}
