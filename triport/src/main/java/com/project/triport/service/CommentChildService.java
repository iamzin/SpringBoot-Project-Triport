package com.project.triport.service;

import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentParent;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.CommentChildRepository;
import com.project.triport.repository.CommentParentRepository;
import com.project.triport.requestDto.CommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

    // BoardCommentParent 의 답글 페이징 조회
    public ResponseDto getPagedCommentChildList(Long commentParentId, int page) {

        // DB에서 해당 BoardCommentParent 조회
        CommentParent commentParent = commentParentRepository.findById(commentParentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
                );

        // page 관련 request 설정
        PageRequest pageRequest = PageRequest.of(page-1,5);

        // 페이징 처리된 BoardCommentChild 리스트 조회
        Slice<CommentChild> boardCommentChildPage = commentChildRepository.findByCommentParent(commentParent, pageRequest);

        // 마지막 페이지 여부 설정
        Boolean isLast = boardCommentChildPage.isLast();

        // Response의 results에 담길 DtoList 객체 생성
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        // 페이징된 BoardCommentChild 리스트를 Dto로 변환
        for (CommentChild commentChild : boardCommentChildPage) {
            CommentResponseDto responseDto = new CommentResponseDto(commentChild);
            responseDtoList.add(responseDto);
        }

        return new ResponseDto(true, responseDtoList, "해당 댓글의 답글 페이징 리스트 조회에 성공하였습니다.", isLast);
    }

    // BoardCommentChild 작성
    public ResponseDto createCommentChild(Long commentParentId, CommentRequestDto requestDto) {
        // DB에서 해당 BoardCommentParent 조회
        CommentParent commentParent = commentParentRepository.findById(commentParentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
                );

        // "member": 현재 로그인한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        CommentChild commentChild = new CommentChild(requestDto, commentParent, member);
        System.out.println("boardCommentChild.getContents() = " + commentChild.getContents());
        commentChildRepository.save(commentChild);

        return new ResponseDto(true, "답글 작성이 완료되었습니다.");
    }

    // BasicCommentChild 수정
    @Transactional
    public ResponseDto updateCommentChild(Long commentChildId, CommentRequestDto requestDto) {
        CommentChild commentChild = commentChildRepository.findById(commentChildId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 답글이 존재하지 않습니다.")
                );
        // "member": 현재 로그인한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        // 답글 작성자가 맞는지 검증
        if(member.getId().equals(commentChild.getMember().getId())) {
            commentChild.update(requestDto);
            return new ResponseDto(true, "답글 수정이 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }

    // BasicCommentChild 삭제
    @Transactional
    public ResponseDto deleteCommentChild(Long commentChildId) {
        CommentChild commentChild = commentChildRepository.findById(commentChildId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 답글이 존재하지 않습니다.")
                );

        // "member": 현재 로그인한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        // 답글 작성자가 맞는지 검증
        if(member.getId().equals(commentChild.getMember().getId())) {
            commentChildRepository.deleteById(commentChildId);
            return new ResponseDto(true, "답글 삭제가 완료되었습니다.");
        } else {
            return new ResponseDto(false, "유저 정보가 일치하지 않습니다.");
        }
    }
}
