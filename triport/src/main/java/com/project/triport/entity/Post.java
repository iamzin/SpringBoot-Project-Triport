package com.project.triport.entity;

import com.project.triport.requestDto.VideoUrlDto;
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
    private Boolean posPlay;

    @Column(nullable = false)
    private Long likeNum;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE})
    private List<PostHashtag> hashtag = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE})
    private List<PostLike> postLike = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    public Post(String videoType, String videoUrl, boolean posPlay, Member member) {
        this.videoType = videoType;
        this.videoUrl = videoUrl;
        this.posPlay = posPlay;
        this.likeNum = 0L;
        this.member = member;
    }

    public void addHashtagAll(List<PostHashtag> hashtagList){
        this.hashtag.addAll(hashtagList);
    }
    public void update(List<PostHashtag> hashtagList) {
        this.hashtag.clear();
        this.hashtag.addAll(hashtagList);
    }

    public void updateUrl(VideoUrlDto requestDto) {
        String[] videoUrlStringList = requestDto.getVideoUrl().split("\\.");
        this.videoType = videoUrlStringList[videoUrlStringList.length-1];
        this.posPlay = true;
        this.videoUrl = requestDto.getVideoUrl();
    }

    public void plusLikeNum() {
        this.likeNum++;
    }

    public void minusLikeNum() {
        this.likeNum--;
    }

}
