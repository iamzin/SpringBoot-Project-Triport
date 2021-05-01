package com.project.triport.entity;

import com.project.triport.requestDto.BoardRequestDto;
//import com.project.triport.requestDto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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
    private String imgUrl;

    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false)
    private Long likeNum;

    @Column(nullable = false)
    private Long commentNum;

    @Column(nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; //user의 nickname, profileImgUrl

    public Board(BoardRequestDto boardRequestDto, Member member){
        this.title = boardRequestDto.getTitle();
        this.description = boardRequestDto.getDescription();
        this.imgUrl = boardRequestDto.getImgUrl();
        this.videoUrl = boardRequestDto.getVideoUrl();
        this.likeNum = 0L;
        this.commentNum = 0L;
        this.address = boardRequestDto.getAddress();
        this.member = member;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.description = boardRequestDto.getDescription();
        this.imgUrl = boardRequestDto.getImgUrl();
        this.videoUrl = boardRequestDto.getVideoUrl();
        this.address = boardRequestDto.getAddress();
    }

    public void updateCommentNum(int count) {
        this.commentNum += count;
    }

    public void updateLikeNum(int count) {
        this.likeNum += count;
    }
}
