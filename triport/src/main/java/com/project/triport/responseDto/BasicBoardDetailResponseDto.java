package com.project.triport.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.triport.entity.BasicBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BasicBoardDetailResponseDto {

    private boolean ok;
    private Object results;
    private String msg;

    /*
        information
     */
    @Setter
    @Getter
    @NoArgsConstructor
    public static class BasicBoardDetailInformationDto {
        // BasicBoard
        private Long basicId;
        private String basicTitle;
        private String basicDescription; // "인천 앞바다 갈매기와 한 컷"
        private String basicImgUrl; // http://s3.soghoshg
        private String basicVideoUrl; // http://s3.soghoshg
        private Long basicLikeNum; // Like 총 개수
        private Long basicCommentNum; // comment 총 개수
        private String basicAddress; // 주소
        private String modifiedAt; // "2021-04-24 16:25"
    }

    /*
        author
     */
    @Setter
    @Getter
    @NoArgsConstructor
    public static class BasicBoardDetailAuthorDto {
        private String nickname; // email 주소(id)
        private String profileImgUrl; // http://15.165.205.40/profiles/img1.png
    }

    /*
        user
     */
    @Setter
    @Getter
    @NoArgsConstructor
    public static class BasicBoardDetailUserDto {
        private Boolean isLike;
    }

    /*
        commentList
     */
    @Setter
    @Getter
    @NoArgsConstructor
    public static class BasicBoardDetailCommentListDto {
        private List<BasicBoardCommentResponseDto> basicBoardCommentResponseDtoList;
    }
}
