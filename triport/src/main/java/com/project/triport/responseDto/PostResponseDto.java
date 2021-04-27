package com.project.triport.responseDto;

import com.project.triport.entity.Post;
import com.project.triport.entity.User;

import java.time.LocalDateTime;

public class PostResponseDto {
    // post 관련 전달 사항
    private Long id;
    private String postDescription; // "인천 앞바다 갈매기와 한 컷"
    private String postImgUrl; // http://15.165.205.40/images/img1.png
    private Long postLikeNum; // Like 총 개수
    private Long postCommentNum; // comment 총 개수
    private String modifiedAt; // "2021-04-24T16:25:30.013"
                                // "yyyy-mm-dd kk:mm"

    // like 관련 전달 사항
    private Boolean isLike; // 열람하는 user가 좋아요 했으면 true 아니면 false

    // user 관련 전달 사항
    private String nickname; // email 주소(id)
    private String profileImgUrl; // http://15.165.205.40/profiles/img1.png

    public PostResponseDto(Post post, User user) {
        this.id = post.getId();
        this.postDescription = post.getDescription();
        this.postImgUrl = post.getImgUrl();
        this.postLikeNum = post.getLikeNum();
        this.postCommentNum = post.getCommentNum();

//        this.modifiedAt = post.getModifiedAt(); // formatter 사용하여 String 형태로 보내는지?

//        this.isLike  // 생성자를 통해 생성 시 DB 접근하여 확인 필요한가?? 전부 다??

        this.nickname = user.getNickname();
        this.profileImgUrl = user.getProfileImgUrl();
    }
}
