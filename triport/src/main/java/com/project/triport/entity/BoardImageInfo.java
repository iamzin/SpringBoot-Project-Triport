package com.project.triport.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class BoardImageInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // s3 이미지 파일 경로 (Key)
    @Column(nullable = false)
    private String filePath;

    // 삭제될 이미지인지 판단
    @Column(nullable = false)
    private Boolean shouldBeDeleted;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardImageInfo(Member member, String filePath) {
        this.member = member;
        this.filePath = filePath;
        this.shouldBeDeleted = true;
    }

    public BoardImageInfo(Member member, String filePath, Board board) {
        this.filePath = filePath;
        this.shouldBeDeleted = true;
        this.member = member;
        this.board = board;
        board.getBoardImageInfoList().add(this);
    }

    public void updateRelationWithBoard(Board board) {
        this.board = board;
        board.getBoardImageInfoList().add(this);
    }

    public void updateShouldBeDelete(boolean shouldBeDeleted) {
        this.shouldBeDeleted = shouldBeDeleted;
    }
}
