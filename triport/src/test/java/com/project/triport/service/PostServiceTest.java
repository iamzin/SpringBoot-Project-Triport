package com.project.triport.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.triport.entity.User;
import com.project.triport.repository.UserRepository;
import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.requestDto.UserRequestDto;
import com.project.triport.responseDto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    UserRepository userRepository;

    @Test
    public void readPostsAll() throws JsonProcessingException {
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
//        // page 설정
//        int page = 1;
//        String filter = "modifiedAt";
        ResponseDto responseDto1 = postService.readPostsAll(user1,0,"modifiedAt");
        ResponseDto responseDto2 = postService.readPostsAll(user1,1,"modifiedAt");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson1 = objectMapper.writeValueAsString(responseDto1);
        String resultJson2 = objectMapper.writeValueAsString(responseDto2);
        System.out.println(resultJson1);
        System.out.println(resultJson2);
    }


    }
