package com.project.triport.entity;

import com.project.triport.requestDto.BasicBoardRequestDto;
//import com.project.triport.requestDto.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
public class BasicBoard extends Timestamped { //basicBoard에서 지도 주소 값 column 필요
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
    @JoinColumn(name = "user_id")
    private User user; //user의 nickname, profileImgUrl

    public BasicBoard(BasicBoardRequestDto basicBoardRequestDto, User user){
        this.title = basicBoardRequestDto.getTitle();
        this.description = basicBoardRequestDto.getDescription();
        this.imgUrl = basicBoardRequestDto.getImgUrl();
        this.videoUrl = basicBoardRequestDto.getVideoUrl();
        this.likeNum = basicBoardRequestDto.getLikeNum();
        this.commentNum = 0L;
        this.address = basicBoardRequestDto.getAddress();
        this.user = user;
    }

    public void update(BasicBoardRequestDto basicBoardRequestDto) {
        this.title = basicBoardRequestDto.getTitle();
        this.description = basicBoardRequestDto.getDescription();
        this.imgUrl = basicBoardRequestDto.getImgUrl();
        this.videoUrl = basicBoardRequestDto.getVideoUrl();
        this.likeNum = basicBoardRequestDto.getLikeNum();
        this.address = basicBoardRequestDto.getAddress();
    }

    public void updateCommentNum(int count) {
        this.commentNum += count;
    }
}
