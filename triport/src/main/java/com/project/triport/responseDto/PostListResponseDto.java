package com.project.triport.responseDto;

import com.project.triport.entity.Post;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostListResponseDto {

    private Boolean ok;
    private List<PostResponseDto> results;
    private String msg;
    private Boolean last; // 마지막 postId일 때 true, 이외에는 false

    public PostListResponseDto(Boolean ok, List<PostResponseDto> results, String msg, Boolean last){
        this.ok = ok;
        this.results = results;
        this.msg = msg;
        this.last = last;
    }

    public static class PostResponseDto{
        // post 관련 전달 사항
        private Long id;
        private String postDescription; // "인천 앞바다 갈매기와 한 컷"
        private String postImgUrl; // http://15.165.205.40/images/img1.png
        private Long postLikeNum; // Like 총 개수
        private Long postCommentNum; // comment 총 개수
        private String modifiedAt; // "2021-04-24 16:25"

        // like 관련 전달 사항
        private Boolean isLike; // 열람하는 user가 좋아요 했으면 true 아니면 false

        // user 관련 전달 사항
        private String nickname; // email 주소(id)
        private String profileImgUrl; // http://15.165.205.40/profiles/img1.png

        public PostResponseDto(Post post, User user){

        }
    }

    public List<PostResponseDto> makePostResponseDtoList(List<Post> postList){
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();


        return postResponseDtoList;
    }
}
