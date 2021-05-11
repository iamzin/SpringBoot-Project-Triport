package com.project.triport.entity;

import com.project.triport.requestDto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String videoType;

    @Column(nullable = false)
    private String videoUrl;

    @Column(nullable = false)
    private Long likeNum;

    @ElementCollection
    private List<String> hashtag;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

//    public Post(PostRequestDto requestDto, Member member){
//        this.videoUrl = requestDto.getVideoUrl();
//        this.likeNum = 0L;
//        this.hashtag = requestDto.getHashtag();
//        this.member = member;
//    }

    public Post(String videoUrl, List<String> hashtag, Member member) {
        this.videoType = "m3u8";
        this.videoUrl = videoUrl;
        this.likeNum = 0L;
        this.hashtag = hashtag;
        this.member = member;
    }

    public void update(PostRequestDto requestDto) {
        this.hashtag = requestDto.getHashtag();
    }

    public void plusLikeNum() {
        this.likeNum++;
    }

    public void minusLikeNum() {
        this.likeNum--;
    }
}
