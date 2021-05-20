package com.project.triport.service;

import com.project.triport.entity.CommentParent;
import com.project.triport.entity.CommentParentLike;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.CommentParentLikeRepository;
import com.project.triport.repository.CommentParentRepository;
import com.project.triport.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentParentLikeService {
    private final CommentParentLikeRepository commentParentLikeRepository;
    private final CommentParentRepository commentParentRepository;

    @Transactional
    public ResponseDto CreateAndDeleteCommentParentLike(Long commentParentId) {
        CommentParent commentParent = commentParentRepository.findById(commentParentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당 댓글 정보가 없습니다.")
                );

        // "member": 현재 로그인한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        boolean isExist = commentParentLikeRepository.existsByCommentParentAndMember(commentParent, member);

        if (isExist) {
            commentParentLikeRepository.deleteByCommentParentAndMember(commentParent, member);
            commentParent.updateLikeNum(-1);
            return new ResponseDto(true, "댓글 좋아요 취소가 완료되었습니다.",200);
        } else {
            CommentParentLike commentParentLike = new CommentParentLike(commentParent, member);
            commentParentLikeRepository.save(commentParentLike);
            commentParent.updateLikeNum(+1);
            return new ResponseDto(true, "댓글 좋아요 추가가 완료되었습니다.",200);
        }
    }
}
