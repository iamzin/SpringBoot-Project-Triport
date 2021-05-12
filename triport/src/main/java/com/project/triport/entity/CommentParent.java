package com.project.triport.entity;

import com.project.triport.requestDto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class CommentParent extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Long likeNum;

    @Column(nullable = false)
    private Long commentChildNum;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "commentParent", cascade = {CascadeType.REMOVE})
    private List<CommentChild> commentChildList = new ArrayList<>();

    @OneToMany(mappedBy = "commentParent", cascade = {CascadeType.REMOVE})
    private List<CommentParentLike> commentParentLikeList = new ArrayList<>();

    public CommentParent(CommentRequestDto commentRequestDto, Board board, Member member) {
        this.contents = commentRequestDto.getContents();
        this.likeNum = 0L;
        this.commentChildNum = 0L;
        this.board = board;
        board.getCommentParentList().add(this);
        this.member = member;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }

    public void updateLikeNum(int count) {
        this.likeNum += count;
    }

    public void updateCommentChildNum(int count) {
        this.commentChildNum += count;
    }
}
