//package com.project.triport.service;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.triport.entity.BasicBoard;
//import com.project.triport.entity.BasicBoardComment;
//import com.project.triport.entity.User;
//import com.project.triport.repository.BasicBoardCommentRepository;
//import com.project.triport.repository.BasicBoardRepository;
//import com.project.triport.repository.UserRepository;
//import com.project.triport.requestDto.BasicBoardCommentRequestDto;
//import com.project.triport.requestDto.BasicBoardRequestDto;
//import com.project.triport.requestDto.UserRequestDto;
//import com.project.triport.responseDto.ResponseDto;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class BasicBoardCommentServiceTest {
//
//    @Autowired
//    BasicBoardService basicBoardService;
//
//    @Autowired
//    BasicBoardCommentService basicBoardCommentService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    BasicBoardRepository basicBoardRepository;
//
//    @Autowired
//    BasicBoardCommentRepository basicBoardCommentRepository;
//
//    @Test
//    public void getPagedBasicBoardCommentList() throws Exception {
//        //given
//        UserRequestDto userDto1 = new UserRequestDto("cowlsdnr77@naver.com", "1234", "진욱", "profileImgUrl입니다.", "USER");
//        User user1 = new User(userDto1);
//        userRepository.save(user1);
//
//        BasicBoardRequestDto basicBoardRequestDto1 = new BasicBoardRequestDto("강릉 앞바다", "강릉 앞바다 멋지당", "imgurl입니다.", "videourl입니다.", "강릉 어딘가");
//        BasicBoard basicBoard1 = new BasicBoard(basicBoardRequestDto1, user1);
//        basicBoardRepository.save(basicBoard1);
//
//        //when
//        for(int i=0 ; i<10 ; i++) {
//            BasicBoardCommentRequestDto basicBoardCommentRequestDto = new BasicBoardCommentRequestDto("댓글테스트" + i +" 입니다");
//            basicBoardCommentService.createBasicBoardComment(basicBoard1.getId(), basicBoardCommentRequestDto, user1);
//        }
//
//        //then
//        ResponseDto responseDto1 = basicBoardCommentService.getPagedBasicBoardCommentList(basicBoard1.getId(),1);
//        ResponseDto responseDto2 = basicBoardCommentService.getPagedBasicBoardCommentList(basicBoard1.getId(),2);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
//        String resultJson1 = objectMapper.writeValueAsString(responseDto1);
//        String resultJson2 = objectMapper.writeValueAsString(responseDto2);
//        System.out.println(resultJson1);
//        System.out.println(resultJson2);
//    }
//
//    @Test
//    public void createBasicBoardComment() throws Exception {
//        //given
//        UserRequestDto userDto1 = new UserRequestDto("cowlsdnr77@naver.com", "1234", "진욱", "profileImgUrl입니다.", "USER");
//        User user1 = new User(userDto1);
//        userRepository.save(user1);
//
//        BasicBoardRequestDto basicBoardRequestDto1 = new BasicBoardRequestDto("강릉 앞바다", "강릉 앞바다 멋지당", "imgurl입니다.", "videourl입니다.", "강릉 어딘가");
//        BasicBoard basicBoard1 = new BasicBoard(basicBoardRequestDto1, user1);
//        basicBoardRepository.save(basicBoard1);
//
//        //when
//        BasicBoardCommentRequestDto basicBoardCommentRequestDto = new BasicBoardCommentRequestDto("댓글테스트입니다");
//        basicBoardCommentService.createBasicBoardComment(basicBoard1.getId(), basicBoardCommentRequestDto, user1);
//
//        BasicBoardCommentRequestDto basicBoardCommentRequestDto1 = new BasicBoardCommentRequestDto("댓글테스트1입니다");
//        basicBoardCommentService.createBasicBoardComment(basicBoard1.getId(), basicBoardCommentRequestDto1, user1);
//
//        //then
//        ResponseDto responseDto = basicBoardService.getBasicBoardDetail(user1,1L);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
//        String resultJson = objectMapper.writeValueAsString(responseDto);
//        System.out.println(resultJson);
//    }
//
//    @Test
//    public void updateBasicBoardComment() throws Exception {
//        //given
//        UserRequestDto userDto1 = new UserRequestDto("cowlsdnr77@naver.com", "1234", "진욱", "profileImgUrl입니다.", "USER");
//        User user1 = new User(userDto1);
//        userRepository.save(user1);
//
//        BasicBoardRequestDto basicBoardRequestDto1 = new BasicBoardRequestDto("강릉 앞바다", "강릉 앞바다 멋지당", "imgurl입니다.", "videourl입니다.","강릉 어딘가");
//        BasicBoard basicBoard1 = new BasicBoard(basicBoardRequestDto1, user1);
//        basicBoardRepository.save(basicBoard1);
//
//        //when
//        BasicBoardCommentRequestDto basicBoardCommentRequestDto = new BasicBoardCommentRequestDto("댓글테스트입니다");
//        basicBoardCommentService.createBasicBoardComment(basicBoard1.getId(), basicBoardCommentRequestDto, user1);
//
//        //then
//        BasicBoardCommentRequestDto basicBoardCommentRequestDto1 = new BasicBoardCommentRequestDto("댓글테스트수정입니다");
//        basicBoardCommentService.updateBasicBoardComment(1L, basicBoardCommentRequestDto1, user1);
//
//        ResponseDto responseDto = basicBoardService.getBasicBoardDetail(user1,1L);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); //json 변환 이슈 해결: https://steady-hello.tistory.com/90
//        String resultJson = objectMapper.writeValueAsString(responseDto);
//        System.out.println(resultJson);
//
//    }
//
//}