package com.project.triport.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.User;
import com.project.triport.repository.BasicBoardLikeRepository;
import com.project.triport.repository.BasicBoardRepository;
import com.project.triport.repository.UserRepository;
import com.project.triport.requestDto.BasicBoardRequestDto;
import com.project.triport.requestDto.UserRequestDto;
import com.project.triport.responseDto.ResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BasicBoardLikeServiceTest {

    @Autowired
    BasicBoardService basicBoardService;

    @Autowired
    BasicBoardLikeService basicBoardLikeService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BasicBoardRepository basicBoardRepository;

    @Autowired
    BasicBoardLikeRepository basicBoardLikeRepository;

    @Test
    public void CreateAndDeleteLike() throws Exception {
        //given
        UserRequestDto userDto1 = new UserRequestDto("cowlsdnr77@naver.com", "1234", "진욱", "profileImgUrl입니다.", "USER");
        User user1 = new User(userDto1);
        userRepository.save(user1);

        UserRequestDto userDto2 = new UserRequestDto("rtan@naver.com", "1234", "르탄이", "profileImgUrl입니다.", "USER");
        User user2 = new User(userDto2);
        userRepository.save(user2);

        BasicBoardRequestDto basicBoardRequestDto1 = new BasicBoardRequestDto("강릉 앞바다", "강릉 앞바다 멋지당", "imgurl입니다.", "videourl입니다.", "강릉 어딘가");
        BasicBoard basicBoard1 = new BasicBoard(basicBoardRequestDto1, user1);
        basicBoardRepository.save(basicBoard1);

        //when
        basicBoardLikeService.CreateAndDeleteBasicBoardLike(basicBoard1.getId(), user1); // 처음 누른경우 -> true
//        basicBoardLikeService.CreateAndDeleteBasicBoardLike(basicBoard1.getId(), user1); // 다시 누른경우 -> false

        //then
        ResponseDto responseDto = basicBoardService.getBasicBoardList(user2,1,"modifiedAt");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);
    }

}