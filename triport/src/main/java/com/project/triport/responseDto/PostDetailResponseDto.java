package com.project.triport.responseDto;

import com.project.triport.entity.Post;
import com.project.triport.entity.PostComment;
import com.project.triport.entity.User;
import com.project.triport.requestDto.PostCommentDto;

import java.util.ArrayList;
import java.util.List;

public class PostDetailResponseDto {
    // post 관련 전달 사항
    private PostResponseDto postResponseDto;
    private Long id;
    private String postDescription; // "인천 앞바다 갈매기와 한 컷"
    private String postImgUrl; // http://15.165.205.40/images/img1.png
    private Long postLikeNum; // Like 총 개수
    private Long postCommentNum; // comment 총 개수
    private List<PostCommentResponseDto> postCommentList;
    private String modifiedAt; // "2021-04-24 16:25"

    // like 관련 전달 사항
    private Boolean isLike; // 열람하는 user가 좋아요 했으면 true 아니면 false

    // user 관련 전달 사항
    private String nickname; // email 주소(id)
    private String profileImgUrl; // http://15.165.205.40/profiles/img1.png

    public PostDetailResponseDto(Post post) {
        this.id = post.getId();
        this.postDescription = post.getDescription();
        this.postImgUrl = post.getImgUrl();
        this.postLikeNum = post.getLikeNum();
        this.postCommentNum = post.getCommentNum();
        // 빈 리스트를 만들어 준다.
        List<PostCommentResponseDto> postCommentResponseDtoList = new ArrayList<>();
        // commentList 안에 있는 comment에서 원하는 값을 걸러내 commentResponseDto에 넣어준다.
        for(PostComment postComment : post.getCommentList()) {
            PostCommentResponseDto postCommentResponseDto = new PostCommentResponseDto(postComment);
            // 빈 리스트에 commentResponseDto를 넣어준다.
            postCommentResponseDtoList.add(postCommentResponseDto);
        }
        this.postCommentList = postCommentResponseDtoList;

        this.nickname = post.getUser().getNickname();
        this.profileImgUrl = post.getUser().getProfileImgUrl();

//    댓글 작성자 nickname, profileImg 도 들어가야 한다. 댓글용 Dto 따로 파야겠지??;
//    {
//        "commentId" : 35,
//            "nickname" : "user1",
//            "profileImgUrl" : "http://15.165.205.40/profile/3asgaw9h.png",
//            "commentContents" : "사진 정말 잘 찍으시네요~"
//              "user" :{
//                    "email" : "asg",
//                    "profile"
//        }
//      }
//    },
//        this.modifiedAt = post.getModifiedAt(); // formatter 사용하여 String 형태로 보내는지?
//        this.isLike  // 생성자를 통해 생성 시 DB 접근하여 확인 필요한가?? 전부 다??
    }
}
