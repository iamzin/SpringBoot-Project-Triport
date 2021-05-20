package com.project.triport.service;

import com.project.triport.entity.CommentChild;
import com.project.triport.entity.CommentChildLike;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.CommentChildLikeRepository;
import com.project.triport.repository.CommentChildRepository;
import com.project.triport.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentChildLikeService {
    private final CommentChildLikeRepository commentChildLikeRepository;
    private final CommentChildRepository commentChildRepository;

    @Transactional
    public ResponseDto CreateAndDeleteCommentChildLike(Long commentChildId) {
        CommentChild commentChild = commentChildRepository.findById(commentChildId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 대댓글 정보가 없습니다.")
                );

        // "member": 현재 로그인한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        boolean isExist = commentChildLikeRepository.existsByCommentChildAndMember(commentChild, member);

        if (isExist) {
            commentChildLikeRepository.deleteByCommentChildAndMember(commentChild, member);
            commentChild.updateLikeNum(-1);
            return new ResponseDto(true, "대댓글 좋아요 취소가 완료되었습니다.",200);
        } else {
            CommentChildLike commentChildLike = new CommentChildLike(commentChild, member);
            commentChildLikeRepository.save(commentChildLike);
            commentChild.updateLikeNum(+1);
            return new ResponseDto(true, "대댓글 좋아요 추가가 완료되었습니다.",200);
        }
    }
}
