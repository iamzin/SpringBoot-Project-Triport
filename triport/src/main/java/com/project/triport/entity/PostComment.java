package com.project.triport.entity;

import com.project.triport.requestDto.PostCommentDto;
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
    private String commentContents;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    public PostComment(PostCommentDto postCommentDto, User user, Post post){
        this.commentContents = postCommentDto.getCommentContents();
        this.user = user;
        this.post = post;
    }

}
