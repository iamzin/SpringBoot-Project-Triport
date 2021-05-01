package com.project.triport.service;

import com.project.triport.entity.Board;
import com.project.triport.entity.BoardCommentParent;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final BoardCommentParentService boardCommentParentService;

    // Basic 게시글 전체 리스트 조회 -> 페이징 //User는 Authentication으로 수정해야됨
    public ResponseDto getBoardList(int page, String filter) {

        // 로그인한 멤버의 authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // page 관련 request 설정
        PageRequest pageRequest = PageRequest.of(page-1,10, Sort.by(Sort.Direction.DESC, filter));

        // 페이징 처리된 BasicBoard 리스트 조회
        Slice<Board> BoardSlice = boardRepository.findBy(pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = BoardSlice.isLast();

        // Response의 results에 담길 DtoList 객체 생성
        List<ListResponseDto> responseDtoList = new ArrayList<>();

        boolean isLike;

        // 페이징된 BasicBoard 리스트를 Dto로 변환
        for (Board board : BoardSlice) {
            if(authentication.getPrincipal() == null) { // 비로그인 상황
                isLike = false; //비로그인이므로 islike 값은 false
            } else {
                Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        // "commentList": 현재 게시글의 comment 리스트
        List<BoardCommentParent> boardCommentParentList = boardCommentParentService.getBoardCommentList(board.getId());
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        // comment 리스트 -> comment DTO 로 변경
        for (BoardCommentParent boardCommentParent : boardCommentParentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(boardCommentParent);
            commentResponseDtoList.add(commentResponseDto);
        }

        // isLike 로그인/비로그인 구분
        boolean isLike;

        if(authentication.getPrincipal() == null) { // 비로그인 상황
            isLike = false; //비로그인이므로 islike 값은 false
        } else {
            Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
            isLike = boardLikeRepository.existsByBoardAndMember(board, member); //Member는 현재 로그인한 멤버로 변경해야됨
        }

        DetailResponseDto detailResponseDto = new DetailResponseDto(board, isLike, commentResponseDtoList); // user 파리미터는 현재 로그인한 User로 변경되야함

        return new ResponseDto(true, detailResponseDto,"특정 Basic 게시글 조회에 성공하였습니다."); //detailResponseDto
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
    public ResponseDto createBoard(BoardRequestDto requestDto) {

        // "member": 현재 로그인한 유저 정보 -> islike 가져오기 위함
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        Board board = new Board(requestDto, member);
        boardRepository.save(board);

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
    public ResponseDto deleteBoard(Long basicId) {

        Board board = boardRepository.findById(basicId).orElseThrow(
                () -> new IllegalArgumentException("해당 Basic 게시글이 존재하지 않습니다.")
        );

//         "member": 현재 로그인한 유저 정보 -> 게시글 작성자가 맞는지 검증
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();


        if(member.getId().equals(board.getMember().getId())) {
            boardRepository.deleteById(basicId);
            return new ResponseDto(true, "Basic 게시글 삭제가 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }
}
