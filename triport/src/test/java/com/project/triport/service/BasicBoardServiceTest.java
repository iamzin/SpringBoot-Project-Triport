package com.project.triport.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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
import com.project.triport.responseDto.ResponseDto;
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
    public void basicBoardListTest() throws Exception {
        //given
        UserRequestDto userDto1 = new UserRequestDto("cowlsdnr77@naver.com", "1234", "진욱", "profileImgUrl입니다.", "USER");
        User user1 = new User(userDto1);
        userRepository.save(user1);

        UserRequestDto userDto2 = new UserRequestDto("rtan@naver.com", "1234", "르탄이", "profileImgUrl입니다.", "USER");
        User user2 = new User(userDto2);
        userRepository.save(user2);

        BasicBoardRequestDto basicBoardRequestDto1 = new BasicBoardRequestDto("강릉 앞바다", "강릉 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "강릉 어딘가");
        BasicBoard basicBoard1 = new BasicBoard(basicBoardRequestDto1, user1);
        basicBoardRepository.save(basicBoard1);

        BasicBoardRequestDto basicBoardRequestDto2 = new BasicBoardRequestDto("인천 앞바다", "인천 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "인천 소래포구");
        BasicBoard basicBoard2 = new BasicBoard(basicBoardRequestDto2, user2);
        basicBoardRepository.save(basicBoard2);

        //when
        ResponseDto responseDto = basicBoardService.getBasicBoardList(user1,1,"modifiedAt");

        //then
        System.out.println(responseDto.getOk());
        System.out.println(responseDto.getMsg());
        System.out.println(responseDto.getResults());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);
    }

    @Test
    public void basicBoardDetailTest() throws Exception {
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
        ResponseDto responseDto = basicBoardService.getBasicBoardDetail(user,1L);

        //then
        System.out.println(responseDto.getOk());
        System.out.println(responseDto.getMsg());
        System.out.println(responseDto.getResults());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);

    }

    @Test
    public void basicBoardListFromUserTest() throws Exception {
        //given
        UserRequestDto userDto1 = new UserRequestDto("cowlsdnr77@naver.com", "1234", "진욱", "profileImgUrl입니다.", "USER");
        User user1 = new User(userDto1);
        userRepository.save(user1);

        UserRequestDto userDto2 = new UserRequestDto("rtan@naver.com", "1234", "르탄이", "profileImgUrl입니다.", "USER");
        User user2 = new User(userDto2);
        userRepository.save(user2);

        BasicBoardRequestDto basicBoardRequestDto1 = new BasicBoardRequestDto("강릉 앞바다", "강릉 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "강릉 어딘가");
        BasicBoard basicBoard1 = new BasicBoard(basicBoardRequestDto1, user1);
        basicBoardRepository.save(basicBoard1);

        BasicBoardRequestDto basicBoardRequestDto2 = new BasicBoardRequestDto("여수 앞바다", "여수 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "여수시");
        BasicBoard basicBoard2 = new BasicBoard(basicBoardRequestDto2, user1);
        basicBoardRepository.save(basicBoard2);

        BasicBoardRequestDto basicBoardRequestDto3 = new BasicBoardRequestDto("인천 앞바다", "인천 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "인천 소래포구");
        BasicBoard basicBoard3 = new BasicBoard(basicBoardRequestDto3, user2);
        basicBoardRepository.save(basicBoard3);

//        basicBoardService.updateBasicBoard(1L, basicBoardRequestDto3);
//        basicBoardService.deleteBasicBoard(1L);

        //when
        ResponseDto responseDto = basicBoardService.getBasicBoardListCreatedByUser(user1);

        //then
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);
    }
    
    @Test
    public void createBasicBoardTest() throws Exception {
        //given
        UserRequestDto userDto = new UserRequestDto("cowlsdnr77@naver.com", "1234", "rtan", "profileImgUrl입니다.", "USER");
        User user = new User(userDto);
        userRepository.save(user);

        BasicBoardRequestDto basicBoardRequestDto = new BasicBoardRequestDto("인천 앞바다", "인천 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "인천 소래포구");

        //when
        ResponseDto responseDto = basicBoardService.createBasicBoard(user, basicBoardRequestDto);
        
        //then
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);
    }

    @Test
    public void updateBasicBoardTest() throws Exception {
        //given
        UserRequestDto userDto = new UserRequestDto("cowlsdnr77@naver.com", "1234", "rtan", "profileImgUrl입니다.", "USER");
        User user = new User(userDto);
        userRepository.save(user);

        BasicBoardRequestDto basicBoardRequestDto = new BasicBoardRequestDto("인천 앞바다", "인천 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "인천 소래포구");

        basicBoardService.createBasicBoard(user, basicBoardRequestDto);

        BasicBoardRequestDto inputDto = new BasicBoardRequestDto("제주 앞바다", "제주 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "제주시");
        //when
        ResponseDto responseDto = basicBoardService.updateBasicBoard(1L, inputDto);

        //then
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);
    }

    @Test
    public void deleteBasicBoardTest() throws Exception {
        //given
        UserRequestDto userDto = new UserRequestDto("cowlsdnr77@naver.com", "1234", "rtan", "profileImgUrl입니다.", "USER");
        User user = new User(userDto);
        userRepository.save(user);

        BasicBoardRequestDto basicBoardRequestDto = new BasicBoardRequestDto("인천 앞바다", "인천 앞바다 멋지당", "imgurl입니다.", "videourl입니다.",
                0L, 1L, "인천 소래포구");

        basicBoardService.createBasicBoard(user, basicBoardRequestDto);

        //when
        ResponseDto responseDto = basicBoardService.deleteBasicBoard(1L);

        //then
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
        String resultJson = objectMapper.writeValueAsString(responseDto);
        System.out.println(resultJson);
    }

}