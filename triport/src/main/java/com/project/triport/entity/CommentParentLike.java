package com.project.triport.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CommentParentLike extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "comment_parent_id")
    private CommentParent commentParent;

    public CommentParentLike(CommentParent commentParent, Member member) {
        this.commentParent = commentParent;
        commentParent.getCommentParentLikeList().add(this);
        this.member = member;
    }
}
