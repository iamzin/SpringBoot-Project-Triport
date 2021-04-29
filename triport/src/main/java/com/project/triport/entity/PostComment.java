package com.project.triport.entity;

import com.project.triport.requestDto.PostCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class PostComment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String contents;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    public PostComment(Post post, PostCommentRequestDto postCommentDto, User user) {
        this.contents = postCommentDto.getContents();
        this.user = user;
        this.post = post;
    }
}
