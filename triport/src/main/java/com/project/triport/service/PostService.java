package com.project.triport.service;

import com.project.triport.entity.Post;
import com.project.triport.entity.User;
import com.project.triport.repository.PostLikeRepository;
import com.project.triport.repository.PostRepository;
import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import com.project.triport.responseDto.results.ListResponseDto;
import com.project.triport.responseDto.results.property.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    public ResponseDto readPostsAll(User user, int page){
        List<Post> postList = postRepository.findAll();
        List<ListResponseDto> listResponseDtoList = new ArrayList<>();
        for(Post post : postList){
            Boolean isLike = postLikeRepository.existsByPostAndUser(post,user);
            ListResponseDto listResponseDto = new ListResponseDto(post,isLike);
            listResponseDtoList.add(listResponseDto);
        }
        // last paging 넣어줘야함
        return new ResponseDto(true,listResponseDtoList,"전체 post 조회 완료",true);
    }

    public ResponseDto readPost(Long postId, User user){
        // DetailPost 불러온다.
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("post가 존재하지 않습니다.")
        );
        // 해당 post에 작성되어 있는 댓글들을 불러와 response 양식에 맞게 정리한다.
        List<CommentResponseDto> commentResponseDtoList = postCommentService.makeCommentResponseDtoList(post.getCommentList());
        // 접근 User의 좋아요 상태를 확인한다.
        boolean isLike = postLikeRepository.existsByPostAndUser(post,user);
        // detailResponseDto 생성자에 위 세가지 항목을 넣어 results 양식에 맞는 객체를 작성한다..
        DetailResponseDto detailResponseDto = new DetailResponseDto(post,commentResponseDtoList,isLike);
        return new ResponseDto(true, detailResponseDto, "post detail 불러오기 성공");
    }

    public ResponseDto readPostsUser(User user){
        List<Post> postList = postRepository.findByUser(user);
        List<ListResponseDto> listResponseDtoList = new ArrayList<>();
        for(Post post : postList){
            Boolean isLike = postLikeRepository.existsByPostAndUser(post,user);
            ListResponseDto listResponseDto = new ListResponseDto(post,isLike);
            listResponseDtoList.add(listResponseDto);
        }
        // last paging 넣어줘야함
        return new ResponseDto(true,listResponseDtoList,"전체 post 조회 완료",true);    }

    public ResponseDto createPost(PostRequestDto requestDto, User user){
        Post post = new Post(requestDto,user);
        postRepository.save(post);
        return new ResponseDto(true, "포스팅 완료!");
    }

    @Transactional
    public ResponseDto updatePost(){
        return new ResponseDto(true, "포스트 수정 완료!");
    }

    public ResponseDto deletePost(){
        return new ResponseDto(true, "포스트를 삭제 하였습니다.");
    }
}
