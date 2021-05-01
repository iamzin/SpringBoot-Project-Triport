package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.Post;
import com.project.triport.entity.PostLike;
import com.project.triport.repository.PostLikeRepository;
import com.project.triport.repository.PostRepository;
import com.project.triport.responseDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Transactional
    public ResponseDto creatDeletePostLike(Long postId, Member member){
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 post 입니다.")
        );
        boolean isExist = postLikeRepository.existsByPostAndMember(post, member);
        if(isExist){
            postLikeRepository.deleteByPostAndMember(post, member);
            post.plusLikeNum();
            return new ResponseDto(true, "좋아요 취소");
        }else{
            PostLike postLike = new PostLike(post, member);
            postLikeRepository.save(postLike);
            post.minusLikeNum();
            return new ResponseDto(true, "좋아요 완료");
        }
    }
}
