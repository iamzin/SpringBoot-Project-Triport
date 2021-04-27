package com.project.triport.entity;

import com.project.triport.requestDto.PostDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String imgURL;

    @Column(nullable = false)
    private Long likeNum;

    @Column(nullable = false)
    private Long commentNum;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany
    @JoinColumn
    private List<PostComment> commentList;

    public Post(PostDto requestDto, User user){
        this.description = requestDto.getDescription();
        this.imgURL = requestDto.getImgURL();
        this.likeNum = requestDto.getLikeNum();
        this.commentNum = requestDto.getCommentNum();
        this.user = user;
    }

}
