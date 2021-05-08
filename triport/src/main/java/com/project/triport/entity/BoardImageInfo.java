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

    // 게시글 임시 번호
    @Column(nullable = false)
    private String tempId;

    // s3 이미지 파일 경로 (Key) (Cloudfront 아니고 s3)
    @Column(nullable = false)
    private String filePath;

    // 삭제될 이미지인지 판단
    @Column(nullable = false)
    private Boolean shouldBeDeleted;

    // 게시글 fk
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardImageInfo(String tempId, String filePath) {
        this.tempId = tempId;
        this.filePath = filePath;
        this.shouldBeDeleted = true;
    }

    public void updateRelationWithBoard(Board board) {
        this.board = board;
        board.getBoardImageInfoList().add(this);
    }

    public void updateShouldBeDelete() {
        this.shouldBeDeleted = false;
    }
}
