package com.project.triport.repository;

import com.project.triport.entity.Member;
import com.project.triport.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Slice<Post> findAllBy(Pageable pageable);

    Slice<Post> findByHashtagContaining(String hashtag, Pageable pageable);

    Slice<Post> findByHashtag(String hashtag, Pageable pageable);

    List<Post> findByMember(Member member);
}
