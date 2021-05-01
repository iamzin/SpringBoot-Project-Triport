package com.project.triport.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
public class BoardLike extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "basic_board_id")
    private Board board;

    public BoardLike(Board board, Member member) {
        this.board = board;
        this.member = member;
    }
}
