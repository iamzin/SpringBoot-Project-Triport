package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.Post;
import com.project.triport.entity.PostLike;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.PostLikeRepository;
import com.project.triport.repository.PostRepository;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseDto creatDeletePostLike(Long postId) {
        Member member = getAuthMember();
        if (member == null) {
            return new ResponseDto(false, "로그인이 필요합니다.");
        }
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 post 입니다.")
        );
        boolean isExist = postLikeRepository.existsByPostAndMember(post, member);
        if (isExist) {
            postLikeRepository.deleteByPostAndMember(post, member);
            post.minusLikeNum();
            return new ResponseDto(true,makeResponseDto(post,member),"좋아요 취소");
        } else {
            PostLike postLike = new PostLike(post, member);
            postLikeRepository.save(postLike);
            post.plusLikeNum();
            return new ResponseDto(true,makeResponseDto(post,member),"좋아요 완료");
        }
    }

    public DetailResponseDto makeResponseDto(Post post, Member member){
        boolean isLike = postLikeRepository.existsByPostAndMember(post, member);
        boolean isMembers = post.getMember().getId().equals(member.getId());
        return new DetailResponseDto(post, isLike, isMembers);
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
