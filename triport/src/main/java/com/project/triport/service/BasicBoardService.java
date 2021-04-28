package com.project.triport.service;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardComment;
import com.project.triport.entity.BasicBoardLike;
import com.project.triport.entity.User;
import com.project.triport.repository.BasicBoardRepository;
import com.project.triport.requestDto.BasicBoardRequestDto;
import com.project.triport.requestDto.UserDto;
import com.project.triport.responseDto.BasicBoardCommentResponseDto;
import com.project.triport.responseDto.BasicBoardDetailResponseDto;
import com.project.triport.responseDto.BasicBoardListResponseDto;
import com.project.triport.responseDto.MsgResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicBoardService {

    private final BasicBoardRepository basicBoardRepository;
    private final BasicBoardCommentService basicBoardCommentService;

    // Basic 게시글 전체 리스트 조회
    public BasicBoardListResponseDto getBasicBoardList() {
        List<BasicBoard> basicBoards = basicBoardRepository.findAll(); //findAll 값이 없으면 빈 리스트 반환
        List<BasicBoardListResponseDto.BasicBoardResponseDto> results = new ArrayList<>();

        if (basicBoards.size() > 0) {
            for (BasicBoard basicBoard : basicBoards) {
                BasicBoardListResponseDto.BasicBoardResponseDto basicBoardResponseDto = new BasicBoardListResponseDto.BasicBoardResponseDto(basicBoard);
                results.add(basicBoardResponseDto);
            }
        }

        return new BasicBoardListResponseDto(true,results,"모든 basic 게시글 조회에 성공하였습니다.");
    }

    // Basic 게시글 상세 조회
    public BasicBoardDetailResponseDto getBasicBoardDetail(Long id) {

        BasicBoardDetailResponseDto basicBoardDetailResponseDto = new BasicBoardDetailResponseDto();

        Map<String, Object> map = new HashMap<>();

        // "information": 현재 basic 게시글
        BasicBoard basicBoard = basicBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );
        BasicBoardDetailResponseDto.BasicBoardDetailInformationDto basicBoardDetailInformationDto = new BasicBoardDetailResponseDto.BasicBoardDetailInformationDto();
        basicBoardDetailInformationDto.setBasicId(basicBoard.getId());
        basicBoardDetailInformationDto.setBasicTitle(basicBoard.getTitle());
        basicBoardDetailInformationDto.setBasicDescription(basicBoard.getDescription());
        basicBoardDetailInformationDto.setBasicImgUrl(basicBoard.getImgUrl());
        basicBoardDetailInformationDto.setBasicVideoUrl(basicBoard.getVideoUrl());
        basicBoardDetailInformationDto.setBasicLikeNum(basicBoard.getLikeNum()); // 나중에 count 필요
        basicBoardDetailInformationDto.setBasicCommentNum(basicBoard.getCommentNum()); // 나중에 count 필요
        basicBoardDetailInformationDto.setBasicAddress(basicBoard.getAddress());
        basicBoardDetailInformationDto.setModifiedAt(basicBoard.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));

        // "author": board의 user 연관관계 -> 게시글 작성자의 nickname, profileImgUrl 가져오기
        String nickname = basicBoard.getUser().getNickname();
        String profileImgUrl = basicBoard.getUser().getProfileImgUrl();
        BasicBoardDetailResponseDto.BasicBoardDetailAuthorDto basicBoardDetailAuthorDto = new BasicBoardDetailResponseDto.BasicBoardDetailAuthorDto();
        basicBoardDetailAuthorDto.setNickname(nickname);
        basicBoardDetailAuthorDto.setProfileImgUrl(profileImgUrl);

        // "user": 현재 로그인한 유저 정보 -> islike 가져오기 위함
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();


        // "commentList": 현재 게시글의 comment 리스트
        List<BasicBoardComment> basicBoardCommentList = basicBoardCommentService.getBasicBoardCommentList(basicBoard.getId());
        List<BasicBoardCommentResponseDto> basicBoardCommentResponseDtoList = new ArrayList<>();

        // comment 리스트 -> comment DTO 로 변경
        for (BasicBoardComment basicBoardComment : basicBoardCommentList) {
            BasicBoardCommentResponseDto basicBoardCommentResponseDto = new BasicBoardCommentResponseDto();
            basicBoardCommentResponseDto.setCommentId(basicBoardComment.getId());
            basicBoardCommentResponseDto.setNickname(basicBoardComment.getUser().getNickname());
            basicBoardCommentResponseDto.setProfileImgUrl(basicBoardComment.getUser().getProfileImgUrl());
            basicBoardCommentResponseDto.setContents(basicBoardComment.getContents());
            basicBoardCommentResponseDtoList.add(basicBoardCommentResponseDto);
        }

        map.put("information", basicBoardDetailInformationDto);
        map.put("author", basicBoardDetailAuthorDto);
        map.put("commentList", basicBoardCommentResponseDtoList);

        basicBoardDetailResponseDto.setOk(true);
        basicBoardDetailResponseDto.setResults(map);
        basicBoardDetailResponseDto.setMsg("특정 Basic 게시글 조회에 성공하였습니다.");

        return basicBoardDetailResponseDto;
    }

//    // 게시글 작성
//    public MsgResponseDto createBasicBoard(BasicBoardRequestDto requestDto) {
//        // "user": 현재 로그인한 유저 정보 -> islike 가져오기 위함
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
//
//        // 더미 유저
//        UserDto userDto = new UserDto("cowlsdnr77@naver.com", "1234", "jinook", "profileImgUrl입니다.", "tripper");
//        User user = new User(userDto);
//
//        BasicBoard basicBoard = new BasicBoard(requestDto, user);
//        basicBoardRepository.save(basicBoard);
    //테스트 11111 push test
//
//    }
}
