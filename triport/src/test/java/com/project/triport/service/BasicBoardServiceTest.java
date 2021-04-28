package com.project.triport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.triport.entity.BasicBoard;
import com.project.triport.entity.BasicBoardComment;
import com.project.triport.entity.User;
import com.project.triport.repository.BasicBoardCommentRepository;
import com.project.triport.repository.BasicBoardRepository;
import com.project.triport.repository.UserRepository;
import com.project.triport.requestDto.BasicBoardCommentRequestDto;
import com.project.triport.requestDto.BasicBoardRequestDto;
import com.project.triport.requestDto.UserRequestDto;
import com.project.triport.responseDto.BasicBoardDetailResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BasicBoardServiceTest {

    @Autowired
    BasicBoardService basicBoardService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BasicBoardRepository basicBoardRepository;

    @Autowired
    BasicBoardCommentRepository basicBoardCommentRepository;

    @Test
    public void boardTest() throws Exception {
        //given
        UserRequestDto userDto = new UserRequestDto("cowlsdnr77@naver.com", "1234", "rtan", "profileImgUrl입니다.", "USER");
        User user = new User(userDto);
        userRepository.save(user);

        BasicBoardRequestDto basicBoardRequestDto = new BasicBoardRequestDto("인천 앞바다", "인천 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "인천 소래포구");
        BasicBoard basicBoard = new BasicBoard(basicBoardRequestDto, user);
        basicBoardRepository.save(basicBoard);


        BasicBoardCommentRequestDto basicBoardCommentRequestDto = new BasicBoardCommentRequestDto("댓글테스트입니다");
        BasicBoardComment basicBoardComment = new BasicBoardComment(basicBoardCommentRequestDto, basicBoard, user);
        basicBoardCommentRepository.save(basicBoardComment);


        //when
        BasicBoardDetailResponseDto responseDto = basicBoardService.getBasicBoardDetail(1L); //성공

        //then
        System.out.println(responseDto.getMsg());

        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writeValueAsString(responseDto.getResults());
        System.out.println(resultJson);

    }

}