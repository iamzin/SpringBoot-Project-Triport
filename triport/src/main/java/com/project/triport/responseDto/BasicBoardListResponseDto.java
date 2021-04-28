package com.project.triport.responseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.triport.entity.BasicBoard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BasicBoardListResponseDto {
    private boolean ok;
    private List<BasicBoardResponseDto> results; //Object 형으로 수정할 가능성 있음
    private String msg;

    public BasicBoardListResponseDto(Boolean ok, List<BasicBoardResponseDto> results, String msg){
        this.ok = ok;
        this.results = results;
        this.msg = msg;
    }


    public static class BasicBoardResponseDto {
        // BasicBoard
        private Long basicId;
        private String basicDescription; // "인천 앞바다 갈매기와 한 컷"
        private String basicImgUrl; // http://s3.soghoshg
        private String basicVideoUrl; // http://s3.soghoshg
        private Long basicLikeNum; // Like 총 개수
        private Long basicCommentNum; // comment 총 개수
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDateTime modifiedAt; // "2021-04-24 16:25"

        // like
        private Boolean isLike; // 열람하는 user가 좋아요 했으면 true 아니면 false

        // user (BasicBoard의 user 연관관계를 통해 가져옴)
        private String nickname; // email 주소(id)
        private String profileImgUrl; // http://15.165.205.40/profiles/img1.png

        public BasicBoardResponseDto(BasicBoard basicBoard) {
            this.basicId = basicBoard.getId();
            this.basicDescription = basicBoard.getDescription();
            this.basicImgUrl = basicBoard.getImgUrl();
            this.basicVideoUrl = basicBoard.getVideoUrl();
            this.basicLikeNum = basicBoard.getLikeNum();
            this.basicCommentNum = basicBoard.getCommentNum();
            this.modifiedAt = basicBoard.getModifiedAt();

            this.nickname = basicBoard.getUser().getNickname();
            this.profileImgUrl = basicBoard.getUser().getProfileImgUrl();

//            this.isLike = //Authenticaion 객체를 통해 User 정보를 알아내고 basicBoard와 user의 id로 BasicBoardLikeRepository에서 findByBasicBoardAndUser 해야될듯

        }
    }
}
