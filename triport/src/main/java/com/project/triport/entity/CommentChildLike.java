package com.project.triport.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CommentChildLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "comment_child_id")
    private CommentChild commentChild;

    public CommentChildLike(CommentChild commentChild, Member member) {
        this.commentChild = commentChild;
        commentChild.getCommentChildLikeList().add(this);
        this.member = member;
    }
}
