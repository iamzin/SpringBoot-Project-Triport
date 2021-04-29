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
    private String description;

    @Column(nullable = false)
    private String imgUrl;

    @Column(nullable = false)
    private Long likeNum;

    @Column(nullable = false)
    private Long commentNum;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany
    @JoinColumn(name = "POST_ID")
    private List<PostComment> commentList;

    public Post(PostRequestDto requestDto, User user){
        this.description = requestDto.getDescription();
        this.imgUrl = requestDto.getImgURL();
        this.likeNum = requestDto.getLikeNum();
        this.commentNum = requestDto.getCommentNum();
        this.user = user;
        this.commentList = new ArrayList<>();
    }

    public void update(PostRequestDto requestDto){
        this.description = requestDto.getDescription();
        this.imgUrl = requestDto.getImgURL();
        this.likeNum = requestDto.getLikeNum();
        this.commentNum = requestDto.getCommentNum();
    }

    public void addPostComment(PostComment postComment) {
        this.commentList.add(postComment);
        this.commentNum++;
    }

    public void removePostComment(PostComment postComment) {
        this.commentList.remove(postComment);
        this.commentNum--;
    }

    public void plusLikeNum(){
        this.likeNum++;
    }

    public void minusLikeNum(){
        this.likeNum--;
    }
}
