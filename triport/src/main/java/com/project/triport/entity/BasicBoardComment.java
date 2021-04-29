package com.project.triport.entity;

import com.project.triport.requestDto.BasicBoardCommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BasicBoardComment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "basic_board_id")
    private BasicBoard basicBoard;

    public BasicBoardComment(BasicBoardCommentRequestDto basicBoardCommentRequestDto, BasicBoard basicBoard, User user) {
        this.contents = basicBoardCommentRequestDto.getCommentContents();
        this.basicBoard = basicBoard;
        this.user = user;
    }
}
