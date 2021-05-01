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
    private Long likeNum;

    @Column(nullable = false)
    private Long commentNum;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; //user의 nickname, profileImgUrl

    @OneToMany(mappedBy = "board", cascade = {CascadeType.REMOVE}) // 일반적으로 CascadeType.All 을 사용
    private List<BoardCommentParent> BoardCommentParentList = new ArrayList<>(); //양방향 연관관계를 통해 영속성 전이 삭제를 일으키기 위해 설정

    public Board(BoardRequestDto boardRequestDto, Member member){
        this.title = boardRequestDto.getTitle();
        this.description = boardRequestDto.getDescription();
        this.likeNum = 0L;
        this.commentNum = 0L;
        this.member = member;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.description = boardRequestDto.getDescription();
    }

    public void updateCommentNum(int count) {
        this.commentNum += count;
    }

    public void updateLikeNum(int count) {
        this.likeNum += count;
    }
}
