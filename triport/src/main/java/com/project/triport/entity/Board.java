package com.project.triport.entity;

import com.project.triport.requestDto.BoardRequestDto;
//import com.project.triport.requestDto.UserDto;
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

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Long likeNum;

    @Column(nullable = false)
    private Long commentNum;

    @ManyToOne
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
        this.member = member;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.description = boardRequestDto.getDescription();
        this.address = boardRequestDto.getAddress();
    }

    public void updateCommentNum(int count) {
        this.commentNum += count;
    }

    public void updateLikeNum(int count) {
        this.likeNum += count;
    }
}
