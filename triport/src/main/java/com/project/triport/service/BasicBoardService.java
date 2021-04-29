package com.project.triport.service;

import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardComment;
import com.project.triport.entity.User;
import com.project.triport.repository.BasicBoardLikeRepository;
import com.project.triport.repository.BasicBoardRepository;
import com.project.triport.requestDto.BasicBoardRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import com.project.triport.responseDto.results.ListResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicBoardService {

    private final BasicBoardRepository basicBoardRepository;
    private final BasicBoardLikeRepository basicBoardLikeRepository;
    private final BasicBoardCommentService basicBoardCommentService;

    // Basic 게시글 전체 리스트 조회 -> 페이징 //User는 Authentication으로 수정해야됨
    public ResponseDto getBasicBoardList(User user, int page, String filter) {

        // page 관련 request 설정
        PageRequest pageRequest = PageRequest.of(page-1,5, Sort.by(Sort.Direction.DESC, filter));

        // 페이징 처리된 BasicBoard 리스트 조회
        Slice<BasicBoard> basicBoardSlice = basicBoardRepository.findBy(pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = basicBoardSlice.isLast();

        // Response의 results에 담길 DtoList 객체 생성
        List<ListResponseDto> responseDtoList = new ArrayList<>();

        // 페이징된 BasicBoard 리스트를 Dto로 변환
        for (BasicBoard basicBoard : basicBoardSlice) {
            Boolean isLike = basicBoardLikeRepository.existsByBasicBoardAndUser(basicBoard, user); //User는 현재 로그인한 유저로 변경해야됨
            ListResponseDto responseDto = new ListResponseDto(basicBoard, isLike);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList,"전체 Basic 게시글 리스트 조회에 성공하였습니다.", isLast);
    }

    // Basic 게시글 상세 조회
    public ResponseDto getBasicBoardDetail(User user,Long id) {

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

        // isLike 더미 값
        Boolean isLike = basicBoardLikeRepository.existsByBasicBoardAndUser(basicBoard, user);

        DetailResponseDto detailResponseDto = new DetailResponseDto(basicBoard, isLike, commentResponseDtoList); // user 파리미터는 현재 로그인한 User로 변경되야함

        return new ResponseDto(true, detailResponseDto,"특정 Basic 게시글 조회에 성공하였습니다.");
    }


    // 로그인한 User가 작성한 BasicBoard 리스트 조회 // 페이징 가능성 있음
    public ResponseDto getBasicBoardListCreatedByUser(User user) {
        List<BasicBoard> basicBoardList = basicBoardRepository.findByUser(user);
        List<ListResponseDto> responseDtoList = new ArrayList<>();
        for (BasicBoard basicBoard : basicBoardList) {
            Boolean isLike = basicBoardLikeRepository.existsByBasicBoardAndUser(basicBoard, user); //User는 현재 로그인한 유저로 변경해야됨
            ListResponseDto responseDto = new ListResponseDto(basicBoard, isLike);
            responseDtoList.add(responseDto);
        }
        return new ResponseDto(true, responseDtoList, "User가 작성한 전체 Basic 게시글 조회에 성공하였습니다.");
    }


    //     게시글 작성
    public ResponseDto createBasicBoard(User user,BasicBoardRequestDto requestDto) {
        // "user": 현재 로그인한 유저 정보 -> islike 가져오기 위함
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        BasicBoard basicBoard = new BasicBoard(requestDto, user);
        basicBoardRepository.save(basicBoard);

        return new ResponseDto(true, "Basic 게시글 작성 완료");
    }

    //     게시글 수정
    @Transactional
    public ResponseDto updateBasicBoard(Long id, BasicBoardRequestDto requestDto) {
        // "user": 현재 로그인한 유저 정보 -> islike 가져오기 위함
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        BasicBoard basicBoard = basicBoardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 Basic 게시글이 존재하지 않습니다.")
        );

        basicBoard.update(requestDto);

        return new ResponseDto(true, "Basic 게시글 수정 완료");
    }

    //     게시글 삭제
    public ResponseDto deleteBasicBoard(Long id) {
        // "user": 현재 로그인한 유저 정보 -> islike 가져오기 위함
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        basicBoardRepository.deleteById(id);

        return new ResponseDto(true, "Basic 게시글 삭제 완료");
    }
}
