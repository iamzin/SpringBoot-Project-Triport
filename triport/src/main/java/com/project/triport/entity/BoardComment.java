package com.project.triport.entity;

import com.project.triport.requestDto.BoardCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BoardComment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "basic_board_id")
    private Board board;

    public BoardComment(BoardCommentRequestDto boardCommentRequestDto, Board board, Member member) {
        this.contents = boardCommentRequestDto.getCommentContents();
        this.board = board;
        this.member = member;
    }

    public void update(BoardCommentRequestDto boardCommentRequestDto) {
        this.contents = boardCommentRequestDto.getCommentContents();
    }
}
