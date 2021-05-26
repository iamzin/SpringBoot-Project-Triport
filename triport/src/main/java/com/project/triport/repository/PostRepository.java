package com.project.triport.repository;

import com.project.triport.entity.Member;
import com.project.triport.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllBy(Pageable pageable);

    Page<Post> findByHashtagContaining(String hashtag, Pageable pageable);

    Page<Post> findByHashtag(String hashtag, Pageable pageable);

    List<Post> findByMember(Member member);

    void deleteAllByMember(Member member);
}
