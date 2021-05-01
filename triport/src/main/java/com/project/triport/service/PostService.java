package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.Post;
import com.project.triport.entity.PostComment;
import com.project.triport.entity.User;
import com.project.triport.repository.PostLikeRepository;
import com.project.triport.repository.PostRepository;
import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import com.project.triport.responseDto.results.ListResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentService postCommentService;

    public ResponseDto readPostsAll(User user, int page, String filter ){
        // paging, sort 정리(page uri, filter(sortBy) uri, size 고정값(10), sort 고정값(DESC))
        Sort sort = Sort.by(Sort.Direction.DESC,filter);
        Pageable pageable = PageRequest.of(page, 10, sort);
        // 전체 post 리스트 조회
        Slice<Post> postPage = postRepository.findAllBy(pageable);
        // 반환 page가 last page인지 확인
        Boolean isLast = postPage.isLast();
        // 필요한 post 정보를 담은 ListResponseDto 를 담기 위한 리스트 생성
        List<ListResponseDto> listResponseDtoList = new ArrayList<>();
        // post와 user 정보를 통해 ListResponseDto에 필요한 정보를 기입(생성자 사용)
        for(Post post : postPage){
            Boolean isLike = postLikeRepository.existsByPostAndUser(post,user);
            ListResponseDto listResponseDto = new ListResponseDto(post,isLike);
            listResponseDtoList.add(listResponseDto);
        }
        return new ResponseDto(true,listResponseDtoList,"전체 post 조회 완료",isLast);
    }

    public ResponseDto readPost(Long postId, User user){
        try {
            // DetailPost 불러온다.
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new IllegalArgumentException("post가 존재하지 않습니다.")
            );
            // 접근 User의 좋아요 상태를 확인한다.
            boolean isLike = postLikeRepository.existsByPostAndUser(post, user);
            // 해당 post에 작성되어 있는 댓글들을 불러와 response 양식에 맞게 정리한다.
            List<PostComment> commentList = post.getCommentList();
            List<CommentResponseDto> commentResponseDtoList = postCommentService.makeCommentResponseDtoList(commentList);
            // detailResponseDto 생성자에 위 세가지 항목을 넣어 results 양식에 맞는 객체를 작성한다..
            DetailResponseDto detailResponseDto = new DetailResponseDto(post, commentResponseDtoList, isLike);
            return new ResponseDto(true, detailResponseDto, "post detail 불러오기 성공");
        } catch(IllegalArgumentException e){
            return new ResponseDto(false, e.getMessage());
        }
    }

    public ResponseDto readPostsUser(User user){
        List<Post> postList = postRepository.findByUser(user);
        List<ListResponseDto> listResponseDtoList = new ArrayList<>();
        for(Post post : postList){
            Boolean isLike = postLikeRepository.existsByPostAndUser(post,user);
            ListResponseDto listResponseDto = new ListResponseDto(post,isLike);
            listResponseDtoList.add(listResponseDto);
        }
        return new ResponseDto(true,listResponseDtoList,"전체 post 조회 완료");
    }

    public ResponseDto createPost(PostRequestDto requestDto, Member user){
        Post post = new Post(requestDto,user);
        postRepository.save(post);
        return new ResponseDto(true, "포스팅 완료!");
    }

    public ResponseDto deletePost(Long postId){
        postRepository.deleteById(postId);
        return new ResponseDto(true, "포스트를 삭제 하였습니다.");
    }

    @Transactional
    public ResponseDto updatePost(PostRequestDto requestDto, Long postId){
        // 앞서 Optional<>로 정의되어있는 경우 가져다 사용할 때 Optional<>을 사용하면 if,else로 예외처리 가능할 듯
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 post가 존재하지 않습니다.")
        );
        post.update(requestDto);
        return new ResponseDto(true, "포스트 수정 완료!");
    }
}
