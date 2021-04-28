package com.project.triport.responseDto.results.property.information;

import com.project.triport.entity.Post;

import java.time.format.DateTimeFormatter;

public class PostInformationResponseDto extends InformationResponseDto {
    // post 관련 전달 사항
    private Long id;
    private String description; // "인천 앞바다 갈매기와 한 컷"
    private String imgUrl; // http://15.165.205.40/images/img1.png
    private Long likeNum; // Like 총 개수
    private Long commentNum; // comment 총 개수
    private String modifiedAt; // "2021-04-24T16:25:30.013"
                                // "yyyy-MM-dd kk:mm"

    public PostInformationResponseDto(Post post){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");

        this.id = post.getId();
        this.commentNum = post.getCommentNum();
        this.imgUrl = post.getImgUrl();
        this.likeNum = post.getLikeNum();
        this.commentNum = post.getCommentNum();
        this.modifiedAt = post.getModifiedAt().format(formatter);
    }
}
