package com.project.triport.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
public class BasicBoardLike extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "basic_board_id")
    private BasicBoard basicBoard;

    public BasicBoardLike(BasicBoard basicBoard, Member member) {
        this.basicBoard = basicBoard;
        this.member = member;
    }
}
