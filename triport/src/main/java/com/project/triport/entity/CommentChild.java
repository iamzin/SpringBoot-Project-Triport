package com.project.triport.entity;

import com.project.triport.requestDto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class CommentChild extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // 변경 가능
    @JoinColumn(name = "comment_parent_id")
    private CommentParent commentParent;

    public CommentChild(CommentRequestDto commentRequestDto, CommentParent commentParent, Member member) {
        this.contents = commentRequestDto.getContents();
        this.commentParent = commentParent;
        commentParent.getCommentChildList().add(this);
        this.member = member;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }

}
