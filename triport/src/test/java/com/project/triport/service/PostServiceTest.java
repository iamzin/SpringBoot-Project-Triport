package com.project.triport.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.triport.entity.Post;
import com.project.triport.entity.User;
import com.project.triport.repository.PostCommentRepository;
import com.project.triport.repository.PostRepository;
import com.project.triport.repository.UserRepository;
import com.project.triport.requestDto.PostCommentRequestDto;
import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.requestDto.UserRequestDto;
import com.project.triport.responseDto.ResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostCommentService postCommentService;
    @Autowired
    PostCommentRepository postCommentRepository;

    @Test
    public void readPostsAllTest() throws JsonProcessingException {
        // user 생성
        UserRequestDto userRequestDto1 = new UserRequestDto("asdf@asdf.com","asdf","test1","http://asdf.test","USER");
        User user1 = new User(userRequestDto1);
        userRepository.save(user1);

        UserRequestDto userRequestDto2 = new UserRequestDto("asdf2@asdf.com","asdf2","test2","http://asdf.test","USER");
        User user2 = new User(userRequestDto2);
        userRepository.save(user2);

        for(int i = 1; i < 10; i++){
            // postRequestDto 생성
            PostRequestDto postRequestDto = new PostRequestDto("이것은 테스트지" + i, "http://asdf.test", 3L, 4L);
            // 게시물 생성
            postService.createPost(postRequestDto,user1);
            postService.createPost(postRequestDto,user2);
        }

        ResponseDto responseDto1 = postService.readPostsAll(user1,0,"modifiedAt");
        ResponseDto responseDto2 = postService.readPostsAll(user1,1,"modifiedAt");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson1 = objectMapper.writeValueAsString(responseDto1);
        String resultJson2 = objectMapper.writeValueAsString(responseDto2);
        System.out.println(resultJson1);
        System.out.println(resultJson2);
    }

    @Test
    @Transactional // LazyInitializationException 해결을 위해 붙임. 근데 왜 붙여야하지..
    public void readPostTest() throws JsonProcessingException {
        // user 생성
        UserRequestDto userRequestDto1 = new UserRequestDto("asdf@asdf.com","asdf","test1","http://asdf.test","USER");
        User user = new User(userRequestDto1);
        userRepository.save(user);

        // postRequestDto 생성
        PostRequestDto postRequestDto = new PostRequestDto("이것은 테스트지", "http://asdf.test", 3L, 4L);
        // 게시물 생성
        postService.createPost(postRequestDto,user);

        // 댓글 작성
        for(int i = 0; i < 10; i++) {
            // postCommentRequestDto 생성
            PostCommentRequestDto postCommentRequestDto = new PostCommentRequestDto("이것은 댓글 입니다." + i);
            // 댓글 생성
            postCommentService.createComment(1L, postCommentRequestDto, user);
        }

        // 특정 post 불러오기
        ResponseDto responseDto = postService.readPost(1L, user);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);
    }

    @Test
    @Transactional
    public void readPostsUserTest() throws JsonProcessingException {
        // user 생성
        UserRequestDto userRequestDto1 = new UserRequestDto("asdf@asdf.com","asdf","test1","http://asdf.test","USER");
        User user1 = new User(userRequestDto1);
        userRepository.save(user1);

        UserRequestDto userRequestDto2 = new UserRequestDto("asdf2@asdf.com","asdf2","test2","http://asdf.test","USER");
        User user2 = new User(userRequestDto2);
        userRepository.save(user2);

        for(int i = 1; i < 10; i++){
            // postRequestDto 생성
            PostRequestDto postRequestDto = new PostRequestDto("이것은 테스트지" + i, "http://asdf.test", 3L, 4L);
            // 게시물 생성
            postService.createPost(postRequestDto,user1);
            postService.createPost(postRequestDto,user2);
        }

        ResponseDto responseDto = postService.readPostsUser(user1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);
    }

    @Test
    @Transactional
    public void createUpdateDeleteTest() throws JsonProcessingException {
        // user 생성
        UserRequestDto userRequestDto1 = new UserRequestDto("asdf@asdf.com","asdf","test1","http://asdf.test","USER");
        User user1 = new User(userRequestDto1);
        userRepository.save(user1);

        PostRequestDto postRequestDto = new PostRequestDto("이것은 create 테스트지", "http://asdf.test", 3L, 4L);
        ResponseDto responseDtoCreate = postService.createPost(postRequestDto, user1);
        ResponseDto responseDtoReadCreate = postService.readPost(1L, user1);
        PostRequestDto updateRequestDto = new PostRequestDto("이것은 update 테스트지", "http://asdf.test", 3L, 4L);
        ResponseDto responseDtoUpdate = postService.updatePost(updateRequestDto, 1L);
        ResponseDto responseDtoReadUpdate = postService.readPost(1L, user1);
        ResponseDto responseDtoDelete = postService.deletePost(1L);
        ResponseDto responseDtoReadDelete = postService.readPost(1L, user1);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJsonCreate = objectMapper.writeValueAsString(responseDtoCreate);
        System.out.println(resultJsonCreate);
        String resultJsonReadCreate = objectMapper.writeValueAsString(responseDtoReadCreate);
        System.out.println(resultJsonReadCreate);
        String resultJsonUpdate = objectMapper.writeValueAsString(responseDtoUpdate);
        System.out.println(resultJsonUpdate);
        String resultJsonReadUpdate = objectMapper.writeValueAsString(responseDtoReadUpdate);
        System.out.println(resultJsonReadUpdate);
        String resultJsonDelete = objectMapper.writeValueAsString(responseDtoDelete);
        System.out.println(resultJsonDelete);
        String resultJsonReadDelete = objectMapper.writeValueAsString(responseDtoReadDelete);
        System.out.println(resultJsonReadDelete);

//        {"ok":true,"msg":"포스팅 완료!"}
//        {"ok":true,"results":{"information":{"id":1,"description":"이것은 create 테스트지","imgUrl":"http://asdf.test","likeNum":3,"commentNum":4,"modifiedAt":"2021-04-29 20:58"},"author":{"nickname":"test1","profileImgUrl":"http://asdf.test"},"commentList":[],"user":{"isLike":false}},"msg":"post detail 불러오기 성공"}
//        {"ok":true,"msg":"포스트 수정 완료!"}
//        {"ok":true,"results":{"information":{"id":1,"description":"이것은 update 테스트지","imgUrl":"http://asdf.test","likeNum":3,"commentNum":4,"modifiedAt":"2021-04-29 20:58"},"author":{"nickname":"test1","profileImgUrl":"http://asdf.test"},"commentList":[],"user":{"isLike":false}},"msg":"post detail 불러오기 성공"}
//        {"ok":true,"msg":"포스트를 삭제 하였습니다."}
//        {"ok":false,"msg":"post가 존재하지 않습니다."}
    }
}
