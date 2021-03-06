package com.project.triport.entity;

import com.project.triport.requestDto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped { //basicBoard에서 지도 주소 값 column 필요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Long likeNum;

    @Column(nullable = false)
    private Long commentNum;

    @Column(nullable = false)
    private String thumbNailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = {CascadeType.REMOVE})
    private List<CommentParent> commentParentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = {CascadeType.REMOVE})
    private List<BoardLike> boardLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = {CascadeType.REMOVE})
    private List<BoardImageInfo> boardImageInfoList = new ArrayList<>();

    public Board(BoardRequestDto boardRequestDto, Member member) {
        this.title = boardRequestDto.getTitle();
        this.description = boardRequestDto.getDescription();
        this.address = boardRequestDto.getAddress();
        this.likeNum = 0L;
        this.commentNum = 0L;

        if(boardRequestDto.getImageUrlList().size() > 0) {
            this.thumbNailUrl = boardRequestDto.getImageUrlList().get(0).getImageFilePath();
        } else {
            this.thumbNailUrl = "";
        }

        this.member = member;
    }

    public void update(BoardRequestDto boardRequestDto, String thumbNailUrl) {
        this.title = boardRequestDto.getTitle();
        this.description = boardRequestDto.getDescription();
        this.address = boardRequestDto.getAddress();
        this.thumbNailUrl = thumbNailUrl;
    }

    public void updateCommentNum(int count) {
        this.commentNum += count;
    }

    public void updateLikeNum(int count) {
        this.likeNum += count;
    }
}
