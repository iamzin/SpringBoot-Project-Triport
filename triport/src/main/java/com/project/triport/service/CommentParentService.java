package com.project.triport.service;

import com.project.triport.entity.Board;
import com.project.triport.entity.CommentParent;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.CommentParentLikeRepository;
import com.project.triport.repository.CommentParentRepository;
import com.project.triport.repository.BoardRepository;
import com.project.triport.requestDto.CommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.CommentListResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentParentService {

    private final BoardRepository boardRepository;
    private final CommentParentRepository commentParentRepository;
    private final CommentParentLikeRepository commentParentLikeRepository;

    //Board 상세 페이지 전체 Comment 페이징 조회
    public ResponseDto getPagedCommentParentList(Long boardId, int page) {

        // DB에서 해당 Board 조회
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        //로그인한 멤버의 authentication
        Member member = getAuthMember();

        // page 관련 request 설정
        PageRequest pageRequest = PageRequest.of(page - 1, 5);

        // 페이징 처리된 BoardCommentParent 리스트 조회
        Slice<CommentParent> BoardCommentParentPage = commentParentRepository.findByBoard(board, pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = BoardCommentParentPage.isLast();

        // Response의 results에 담길 DtoList 객체 생성
        List<CommentListResponseDto> responseDtoList = new ArrayList<>();

        // 페이징된 CommentParent 리스트를 Dto로 변환
        for (CommentParent commentParent : BoardCommentParentPage) {
            // 로그인한 멤버의 댓글 좋아요 여부 파악
            boolean isLike = false;
            boolean isMembers = false;
            if (member != null) {
                isLike = commentParentLikeRepository.existsByCommentParentAndMember(commentParent, member);
                isMembers = commentParent.getMember().getId().equals(member.getId());
            }
            CommentListResponseDto responseDto = new CommentListResponseDto(commentParent, isLike, isMembers);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList, "해당 게시글의 댓글 조회에 성공하였습니다.", isLast,200);
    }

    // CommentParent 작성
    @Transactional
    public ResponseDto createCommentParent(Long boardId, CommentRequestDto requestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시판 정보가 없습니다.")
        );

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        CommentParent commentParent = new CommentParent(requestDto, board, member);

        commentParentRepository.save(commentParent);

        board.updateCommentNum(1);

        boolean isLike = commentParentLikeRepository.existsByCommentParentAndMember(commentParent, member);
        boolean isMembers = commentParent.getMember().getId().equals(member.getId());

        CommentListResponseDto responseDto = new CommentListResponseDto(commentParent, isLike, isMembers);

        return new ResponseDto(true, responseDto,"댓글 작성이 완료되었습니다.",200);
    }

    // CommentParent 수정
    @Transactional
    public ResponseDto updateCommentParent(Long commentParentId, CommentRequestDto requestDto) {
        CommentParent commentParent = commentParentRepository.findById(commentParentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
        );

        // "member": 현재 로그인한 유저 정보 -> 댓글 작성자가 맞는지 검증 필요!
        Member member = getAuthMember();

        // 댓글 작성자가 맞는지 검증
        if (member.getId().equals(commentParent.getMember().getId())) {
            commentParent.update(requestDto);

            boolean isLike = commentParentLikeRepository.existsByCommentParentAndMember(commentParent, member);
            boolean isMembers = commentParent.getMember().getId().equals(member.getId());

            CommentListResponseDto responseDto = new CommentListResponseDto(commentParent, isLike, isMembers);

            return new ResponseDto(true, responseDto, "댓글 수정이 완료되었습니다.",200);
        } else {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
        }
    }

    // CommentParent 삭제
    @Transactional
    public ResponseDto deleteCommentParent(Long commentParentId) {
        CommentParent commentParent = commentParentRepository.findById(commentParentId).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
        );

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        // 댓글 작성자가 맞는지 검증
        if (member.getId().equals(commentParent.getMember().getId())) {
            commentParentRepository.deleteById(commentParentId);
            commentParent.getBoard().updateCommentNum(-1);
            return new ResponseDto(true, "댓글 삭제가 완료되었습니다.",200);
        } else {
            throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
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
