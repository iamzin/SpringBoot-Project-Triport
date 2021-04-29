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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "basic_board_id")
    private BasicBoard basicBoard;

    public BasicBoardLike(BasicBoard basicBoard, User user) {
        this.basicBoard = basicBoard;
        this.user = user;
    }
}
