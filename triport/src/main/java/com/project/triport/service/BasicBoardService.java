package com.project.triport.service;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardComment;
import com.project.triport.repository.BasicBoardRepository;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import com.project.triport.responseDto.results.ListResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicBoardService {

    private final BasicBoardRepository basicBoardRepository;
    private final BasicBoardCommentService basicBoardCommentService;

    // Basic 게시글 전체 리스트 조회
    public ResponseDto getBasicBoardList() {
        List<BasicBoard> basicBoards = basicBoardRepository.findAll(); //findAll 값이 없으면 빈 리스트 반환
        List<Object> responseDtoList = new ArrayList<>();

        if (basicBoards.size() > 0) {
            for (BasicBoard basicBoard : basicBoards) {
                ListResponseDto responseDto = new ListResponseDto(basicBoard, basicBoard.getUser()); // user 파라미터 수정 필요
                responseDtoList.add(responseDto);
            }
        }

        return new ResponseDto(true, responseDtoList,"전체 Basic 게시글 리스트 조회에 성공하였습니다.",true);
    }

    // Basic 게시글 상세 조회
    public ResponseDto getBasicBoardDetail(Long id) {

        // DB에서 해당 BasicBoard 조회
        BasicBoard basicBoard = basicBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        // "user": 현재 로그인한 유저 정보 -> islike 가져오기 위함
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();


        // "commentList": 현재 게시글의 comment 리스트
        List<BasicBoardComment> basicBoardCommentList = basicBoardCommentService.getBasicBoardCommentList(basicBoard.getId());
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        // comment 리스트 -> comment DTO 로 변경
        for (BasicBoardComment basicBoardComment : basicBoardCommentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(basicBoardComment);
            commentResponseDtoList.add(commentResponseDto);
        }

        DetailResponseDto detailResponseDto = new DetailResponseDto(basicBoard, basicBoard.getUser(), commentResponseDtoList); // user 파리미터는 현재 로그인한 User로 변경되야함

        return new ResponseDto(true, detailResponseDto,"특정 Basic 게시글 조회에 성공하였습니다.");
    }


    // 게시글 작성
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
