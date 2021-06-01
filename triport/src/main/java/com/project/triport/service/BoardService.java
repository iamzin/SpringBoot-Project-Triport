package com.project.triport.service;

import com.project.triport.entity.*;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.BoardImageInfoRepository;
import com.project.triport.repository.BoardLikeRepository;
import com.project.triport.repository.BoardRepository;
import com.project.triport.requestDto.BoardRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import com.project.triport.responseDto.results.ImageResponseDto;
import com.project.triport.responseDto.results.ListResponseDto;
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
    private final BoardImageInfoService boardImageInfoService;
    private final MemberService memberService;

    // Board 게시글 전체 리스트 조회 -> 페이징
    public ResponseDto getBoardList(int page, String filter, String keyword) {

        // 로그인한 멤버의 authentication
        Member member = getAuthMember();

        // 첫번째 페이지는 size=20, 두번째 페이지부터는 size=5로 만들기
        int size = 0;
        int pageNum = 0;

        if(page-1 == 0) {
            size = 20;
        } else if (page-1 >= 1) {
            pageNum = 2 + page;
            size = 5;
        }

        // page 관련 PageRequest 설정
        PageRequest pageRequest;
        if (filter.equals("likeNum")) {
            pageRequest = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, filter).and(Sort.by(Sort.Direction.DESC,"createdAt")));
        } else {
            pageRequest = PageRequest.of(pageNum, size, Sort.by(Sort.Direction.DESC, filter));
        }


        // 페이징 처리된 검색 결과 Board 리스트 조회
        Slice<Board> BoardSliceSearched = boardRepository.findByTitleContainingOrDescriptionContaining(keyword, keyword, pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = BoardSliceSearched.isLast();

        // Response의 results에 담길 DtoList 객체 생성
        List<ListResponseDto> responseDtoList = new ArrayList<>();


        // 페이징된 BasicBoard 리스트를 Dto로 변환
        for (Board board : BoardSliceSearched) {
            boolean isLike = false;
            boolean isMembers = false;
            if(member != null) { // 비로그인 상황
                isLike = boardLikeRepository.existsByBoardAndMember(board, member);
                isMembers = board.getMember().getId().equals(member.getId());
            }
            ListResponseDto responseDto = new ListResponseDto(board, isLike, isMembers);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList,"전체 Basic 게시글 리스트 조회에 성공하였습니다.", isLast, 200);
    }

    // Basic 게시글 상세 조회
    public ResponseDto getBoardDetail( Long boardId) {
        // DB에서 해당 BasicBoard 조회
        Board board = boardRepository.findById(boardId).orElseThrow(
//                () -> new ApiRequestException("해당 게시글이 존재하지 않습니다.")
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        // "member": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Member member = getAuthMember();

        // isLike 로그인/비로그인 구분
        boolean isLike = false;
        boolean isMembers = false;

        if(member != null) {
            isLike = boardLikeRepository.existsByBoardAndMember(board, member);
            isMembers = board.getMember().getId().equals(member.getId());
        }

        DetailResponseDto detailResponseDto = new DetailResponseDto(board, isLike, isMembers);

        return new ResponseDto(true, detailResponseDto,"특정 게시글 조회에 성공하였습니다.",200);
    }


    // 로그인한 Member가 작성한 Board 리스트 조회 // 페이징 가능성 있음
    public ResponseDto getBoardListCreatedByMember() {
        // "member": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Member member = getAuthMember();

        List<Board> boardList = boardRepository.findByMember(member);

        List<ListResponseDto> responseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            boolean isLike = boardLikeRepository.existsByBoardAndMember(board, member);

            ListResponseDto responseDto = new ListResponseDto(board, isLike, true);
            responseDtoList.add(responseDto);
        }
        return new ResponseDto(true, responseDtoList, "해당 Member가 작성한 전체 게시글 조회에 성공하였습니다.",200);
    }

    // 로그인한 Member가 좋아요 누른 Board 리스트 조회
    public ResponseDto getBoardListMemberLiked() {
        // "member": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Member member = getAuthMember();

        List<BoardLike> boardLikeList = boardLikeRepository.findByMember(member);

        List<Board> boardList = new ArrayList<>();

        List<ListResponseDto> responseDtoList = new ArrayList<>();

        for (BoardLike boardLike : boardLikeList) {
            boardList.add(boardLike.getBoard());
        }

        for (Board board : boardList) {
            boolean isMembers = board.getMember().getId().equals(member.getId());

            ListResponseDto responseDto = new ListResponseDto(board, true, isMembers);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList, "해당 Member가 좋아요 누른 전체 게시글 조회에 성공하였습니다.",200);

    }


    // 게시글 작성
    @Transactional
    public ResponseDto createBoard(BoardRequestDto requestDto) throws IOException {

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        Board board = new Board(requestDto, member);
        boardRepository.save(board);

        // 저장할 게시글에 사용되지 않는 이미지 삭제 (S3와 DB)
        boardImageInfoService.CompareAndDeleteImageForCreate(requestDto.getImageUrlList());

        // BoardImageInfoRepository에서 memberId가 현재 사용자 id이고 BoardId가 null인 BoardImageInfo들
        List<BoardImageInfo> boardImageInfoList = boardImageInfoRepository.findByMemberAndBoardIsNull(member);

        // boardImageInfo에 board 연관관계 설정
        for (BoardImageInfo boardImageInfo : boardImageInfoList) {
            boardImageInfo.updateShouldBeDelete(true); // 이미지 삭제 후에는 게시글 수정 상황을 위해 shouldBeDelete 값을 true로 바꿔놓는다.
            boardImageInfo.updateRelationWithBoard(board);
        }

        // 등업 조건 확인 및 grade up
        String grade = memberService.GradeupMember(member);

        return new ResponseDto(true, "게시글이 작성되었습니다.", grade,200);
    }

    // 게시글 수정
    @Transactional
    public ResponseDto updateBoard(Long boardId, BoardRequestDto requestDto) throws IOException {

        Board board = boardRepository.findById(boardId).orElseThrow(
//                () -> new ApiRequestException("해당 Trilog 게시글이 존재하지 않습니다.")
                () -> new IllegalArgumentException("해당 Trilog 게시글이 존재하지 않습니다.")
        );

        // "member": 현재 로그인한 유저 정보 -> 좋아요 작성자가 맞는지 검증 필요!
        Member member = getAuthMember();


        if(member.getId().equals(board.getMember().getId())) {

            // 수정한 게시글에 사용되지 않는 이미지 삭제 (S3와 DB)
            boardImageInfoService.CompareAndDeleteImageForUpdate(requestDto.getImageUrlList(), board);

            // BoardImageInfoRepository에서 memberId가 현재 사용자의 Id이고 BoardId가 수정중인 게시글 Id인 BoardImageInfo들
            List<BoardImageInfo> boardImageInfoList = boardImageInfoRepository.findByMemberAndBoard(member,board);

            // 이미지 삭제 후에는 게시글 수정 상황을 위해 shouldBeDelete 값을 true로 바꿔놓는다.
            for (BoardImageInfo boardImageInfo : boardImageInfoList) {
                boardImageInfo.updateShouldBeDelete(true);
            }

            String thumbNailUrl = "";

            if(requestDto.getImageUrlList().size() > 0) {
                thumbNailUrl = requestDto.getImageUrlList().get(0).getImageFilePath();
            }

            board.update(requestDto, thumbNailUrl);

            return new ResponseDto(true, "게시글이 수정되었습니다.",200);
        } else {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }
    }

    //     게시글 삭제
    public ResponseDto deleteBoard(Long boardId) throws IOException {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 Trilog 게시글이 존재하지 않습니다.")
        );

        // "member": 현재 로그인한 유저 정보  -> 게시글 작성자가 맞는지 검증
        Member member = getAuthMember();


        if(member.getId().equals(board.getMember().getId())) {
            boardImageInfoService.deleteImageFromS3(board); // s3에서 이미지 파일 삭제
            boardRepository.deleteById(boardId);
            return new ResponseDto(true, "게시글이 삭제되었습니다.",200);
        } else {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }
    }

    // memeber 회원 탈퇴을 위한 board 삭제 method
    // 탈퇴할 member의 board 삭제: boardService에서 진행 (이중 주입 방지)
    // 해당 board 관련 데이터* 모두 삭제
    // *관련 데이터: 해당 board에 업로드된 이미지들, 좋아요, 댓글들 모두 삭제
    public void deleteBoardListFromMember() throws IOException {
        Member member = getAuthMember();
        List<Board> boardList = boardRepository.findByMember(member);
        for (Board board : boardList) {
            boardImageInfoService.deleteImageFromS3(board);
            boardRepository.delete(board);
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
