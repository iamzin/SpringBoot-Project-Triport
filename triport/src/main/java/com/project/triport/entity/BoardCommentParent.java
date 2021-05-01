package com.project.triport.entity;

import com.project.triport.requestDto.BoardCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BoardCommentParent extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // 변경 가능
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardCommentParent(BoardCommentRequestDto boardCommentRequestDto, Board board, Member member) {
        this.contents = boardCommentRequestDto.getCommentContents();
        this.board = board;
        board.getBoardCommentParentList().add(this); // 양방향 연관관계에서 양쪽에 다 값을 입력
        this.member = member;
    }

    public void update(BoardCommentRequestDto boardCommentRequestDto) {
        this.contents = boardCommentRequestDto.getCommentContents();
    }
}
