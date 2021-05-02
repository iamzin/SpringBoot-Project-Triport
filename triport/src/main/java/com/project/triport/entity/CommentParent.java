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
public class CommentParent extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private Long likeNum;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // 변경 가능
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "commentParent", cascade = {CascadeType.REMOVE})
    private List<CommentChild> commentChildList = new ArrayList<>();

    @OneToMany(mappedBy = "commentParent", cascade = {CascadeType.REMOVE})
    private List<CommentParentLike> commentParentLikeList = new ArrayList<>();

    public CommentParent(CommentRequestDto commentRequestDto, Board board, Member member) {
        this.contents = commentRequestDto.getContents();
        this.likeNum = 0L;
        this.board = board;
        board.getCommentParentList().add(this); // 양방향 연관관계에서 양쪽에 다 값을 입력
        this.member = member;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.contents = commentRequestDto.getContents();
    }

    public void updateLikeNum(int count) {
        this.likeNum += count;
    }
}
