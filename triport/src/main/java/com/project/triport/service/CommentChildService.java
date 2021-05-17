package com.project.triport.service;

import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentParent;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.CommentChildLikeRepository;
import com.project.triport.repository.CommentChildRepository;
import com.project.triport.repository.CommentParentRepository;
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
public class CommentChildService {

    private final CommentParentRepository commentParentRepository;
    private final CommentChildRepository commentChildRepository;
    private final CommentChildLikeRepository commentChildLikeRepository;

    // CommentParent 의 답글 페이징 조회
    public ResponseDto getPagedCommentChildList(Long commentParentId, int page) {

        // DB에서 해당 BoardCommentParent 조회
        CommentParent commentParent = commentParentRepository.findById(commentParentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
                );

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        // page 관련 request 설정
        PageRequest pageRequest = PageRequest.of(page - 1, 5);

        // 페이징 처리된 BoardCommentChild 리스트 조회
        Slice<CommentChild> boardCommentChildPage = commentChildRepository.findByCommentParent(commentParent, pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = boardCommentChildPage.isLast();

        // Response의 results에 담길 DtoList 객체 생성
        List<CommentListResponseDto> responseDtoList = new ArrayList<>();

        // 페이징된 BoardCommentChild 리스트를 Dto로 변환
        for (CommentChild commentChild : boardCommentChildPage) {
            boolean isLike = false;
            boolean isMembers = false;
            if (member != null) {
                isLike = commentChildLikeRepository.existsByCommentChildAndMember(commentChild, member);
                isMembers = commentChild.getMember().getId().equals(member.getId());
            }
            CommentListResponseDto responseDto = new CommentListResponseDto(commentChild, isLike, isMembers);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList, "해당 댓글의 대댓글 조회에 성공하였습니다.", isLast);
    }

    // CommentChild 작성
    @Transactional
    public ResponseDto createCommentChild(Long commentParentId, CommentRequestDto requestDto) {
        // DB에서 해당 BoardCommentParent 조회
        CommentParent commentParent = commentParentRepository.findById(commentParentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
                );

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        CommentChild commentChild = new CommentChild(requestDto, commentParent, member);

        commentChildRepository.save(commentChild);

        commentParent.updateCommentChildNum(1);

        boolean isLike = commentChildLikeRepository.existsByCommentChildAndMember(commentChild, member);
        boolean isMembers = commentChild.getMember().getId().equals(member.getId());

        CommentListResponseDto responseDto = new CommentListResponseDto(commentChild, isLike, isMembers);

        return new ResponseDto(true, responseDto,"대댓글 작성이 완료되었습니다.");
    }

    // CommentChild 수정
    @Transactional
    public ResponseDto updateCommentChild(Long commentChildId, CommentRequestDto requestDto) {
        CommentChild commentChild = commentChildRepository.findById(commentChildId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 대댓글이 존재하지 않습니다.")
                );

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        // 답글 작성자가 맞는지 검증
        if (member.getId().equals(commentChild.getMember().getId())) {
            commentChild.update(requestDto);

            boolean isLike = commentChildLikeRepository.existsByCommentChildAndMember(commentChild, member);
            boolean isMembers = commentChild.getMember().getId().equals(member.getId());

            CommentListResponseDto responseDto = new CommentListResponseDto(commentChild, isLike, isMembers);

            return new ResponseDto(true, responseDto,"대댓글 수정이 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }

    // CommentChild 삭제
    @Transactional
    public ResponseDto deleteCommentChild(Long commentChildId) {
        CommentChild commentChild = commentChildRepository.findById(commentChildId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 대댓글이 존재하지 않습니다.")
                );

        // "member": 현재 로그인한 유저 정보
        Member member = getAuthMember();

        // 답글 작성자가 맞는지 검증
        if (member.getId().equals(commentChild.getMember().getId())) {
            commentChildRepository.deleteById(commentChildId);
            commentChild.getCommentParent().updateCommentChildNum(-1);
            return new ResponseDto(true, "대댓글 삭제가 완료되었습니다.");
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
