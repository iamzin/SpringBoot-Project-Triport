package com.project.triport.repository;

import com.project.triport.entity.Post;
import com.project.triport.entity.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    List<PostHashtag> findByHashtagContaining(String hashtag);

    void deleteAllByPost(Post post);
}
