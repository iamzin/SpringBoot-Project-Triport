package com.project.triport.service;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardImageInfo;
import com.project.triport.entity.CommentParent;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.BoardImageInfoRepository;
import com.project.triport.repository.BoardLikeRepository;
import com.project.triport.repository.BoardRepository;
import com.project.triport.requestDto.BoardRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import com.project.triport.responseDto.results.ListResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardImageInfoRepository boardImageInfoRepository;
    private final CommentParentService commentParentService;
    private final BoardImageInfoService boardImageInfoService;

    // Basic 게시글 전체 리스트 조회 -> 페이징 //User는 Authentication으로 수정해야됨
    public ResponseDto getBoardList(int page, String filter, String keyword) {

        // 로그인한 멤버의 authentication
        Member member = getAuthMember();

        // page 관련 request 설정
        PageRequest pageRequest = PageRequest.of(page-1,10, Sort.by(Sort.Direction.DESC, filter));

        // 페이징 처리된 Board 리스트 조회
//        Slice<Board> BoardSlice = boardRepository.findBy(pageRequest);

        // 페이징 처리된 검색 결과 Board 리스트 조회
        Slice<Board> BoardSliceSearched = boardRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword, pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = BoardSliceSearched.isLast();

        // Response의 results에 담길 DtoList 객체 생성
        List<ListResponseDto> responseDtoList = new ArrayList<>();


        // 페이징된 BasicBoard 리스트를 Dto로 변환
        for (Board board : BoardSliceSearched) {
            boolean isLike = false;
            if(member != null) { // 비로그인 상황
                isLike = boardLikeRepository.existsByBoardAndMember(board, member); //Member는 현재 로그인한 멤버로 변경해야됨
            }
            ListResponseDto responseDto = new ListResponseDto(board, isLike);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList,"전체 Basic 게시글 리스트 조회에 성공하였습니다.", isLast);
    }

    // Basic 게시글 상세 조회
    public ResponseDto getBoardDetail( Long basicId) {

        // DB에서 해당 BasicBoard 조회
        Board board = boardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        // "member": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Member member = getAuthMember();


        // "commentList": 현재 게시글의 comment 리스트
        List<CommentParent> commentParentList = commentParentService.getCommentList(board.getId());
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        // comment 리스트 -> comment DTO 로 변경
        for (CommentParent commentParent : commentParentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(commentParent);
            commentResponseDtoList.add(commentResponseDto);
        }

        // isLike 로그인/비로그인 구분
        boolean isLike = false;

        if(member != null) {
            isLike = boardLikeRepository.existsByBoardAndMember(board, member); //Member는 현재 로그인한 멤버로 변경해야됨
        }

        DetailResponseDto detailResponseDto = new DetailResponseDto(board, isLike, commentResponseDtoList); // user 파리미터는 현재 로그인한 User로 변경되야함

        return new ResponseDto(true, detailResponseDto,"특정 게시글 조회에 성공하였습니다."); //detailResponseDto
    }


    // 로그인한 User가 작성한 BasicBoard 리스트 조회 // 페이징 가능성 있음
    public ResponseDto getBoardListCreatedByUser() {
        // "user": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        List<Board> boardList = boardRepository.findByMember(member);
        List<ListResponseDto> responseDtoList = new ArrayList<>();
        for (Board board : boardList) {
            Boolean isLike = boardLikeRepository.existsByBoardAndMember(board, member); //User는 현재 로그인한 유저로 변경해야됨
            ListResponseDto responseDto = new ListResponseDto(board, isLike);
            responseDtoList.add(responseDto);
        }
        return new ResponseDto(true, responseDtoList, "User가 작성한 전체 Basic 게시글 조회에 성공하였습니다.");
    }


    //     게시글 작성
    @Transactional
    public ResponseDto createBoard(BoardRequestDto requestDto) throws IOException {

        // "member": 현재 로그인한 유저 정보 -> islike 가져오기 위함
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        Board board = new Board(requestDto, member);
        boardRepository.save(board);

        // 저장할 게시글에 사용되지 않는 이미지 삭제 (S3와 DB)
        boardImageInfoService.CompareAndDeleteImage(requestDto.getImageUrlList(), requestDto.getTempId());

        // BoardImageInfoRepository에서 requestDto의 tempId와 일치하는 BoardImageInfo들
        List<BoardImageInfo> boardImageInfoList = boardImageInfoRepository.findByTempId(requestDto.getTempId());

        // boardImageInfo에 board 연관관계 설정
        for (BoardImageInfo boardImageInfo : boardImageInfoList) {
            boardImageInfo.updateRelationWithBoard(board); // db 테이블 컬럼 확인 필요!!!
        }


        return new ResponseDto(true, "게시글 작성 완료");
    }

    //     게시글 수정
    @Transactional
    public ResponseDto updateBoard(Long basicId, BoardRequestDto requestDto) {

        Board board = boardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 Basic 게시글이 존재하지 않습니다.")
        );

        // "member": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();


        if(member.getId().equals(board.getMember().getId())) {
            board.update(requestDto);
            return new ResponseDto(true, "Basic 게시글 수정이 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }

    //     게시글 삭제
    public ResponseDto deleteBoard(Long basicId) throws IOException {

        Board board = boardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 Basic 게시글이 존재하지 않습니다.")
        );

//         "member": 현재 로그인한 유저 정보 -> 게시글 작성자가 맞는지 검증
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();


        if(member.getId().equals(board.getMember().getId())) {
            boardImageInfoService.deleteImageFromS3(board); // s3에서 이미지 파일 삭제
            boardRepository.deleteById(basicId);
            return new ResponseDto(true, "Basic 게시글 삭제가 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }

    public Member getAuthMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getMember();
    }
}
