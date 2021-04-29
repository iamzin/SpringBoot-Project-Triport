package com.project.triport.service;

import com.project.triport.entity.Post;
import com.project.triport.entity.PostComment;
import com.project.triport.entity.User;
import com.project.triport.repository.PostCommentRepository;
import com.project.triport.repository.PostRepository;
import com.project.triport.requestDto.PostCommentRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    public ResponseDto createComment(Long postId, PostCommentRequestDto requestDto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 post 입니다.")
        );
        PostComment postComment = new PostComment(post, requestDto, user);
        postCommentRepository.save(postComment);
        postService.addPostComment(post, postComment);
        return new ResponseDto(true, "댓글 작성 완료!");
    }

    public ResponseDto updateComment(Long commentId, PostCommentRequestDto requestDto) {
        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
        postComment.update(requestDto);
        return new ResponseDto(true, "댓글 수정 완료!");
    }

    public ResponseDto deleteComment(Long commentId) {
        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글 입니다.")
        );
        Post post = postComment.getPost();
        postService.removePostComment(post,postComment);
        postCommentRepository.deleteById(commentId);
        return new ResponseDto(true, "댓글을 삭제 하였습니다.");
    }

    public List<CommentResponseDto> makeCommentResponseDtoList(List<PostComment> postCommentList) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (PostComment postComment : postCommentList) {
            commentResponseDtoList.add(new CommentResponseDto(postComment));
        }
        return commentResponseDtoList;
    }
}
