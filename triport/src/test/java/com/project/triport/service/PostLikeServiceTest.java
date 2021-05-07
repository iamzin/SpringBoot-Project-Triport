package com.project.triport.service;

import com.project.triport.entity.Authority;
import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGrade;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.MemberRepository;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.results.DetailResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.ArrayList;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostLikeServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostLikeService postLikeService;
    @Autowired
    PasswordEncoder passwordEncoder;

    // 인증정보 넣어주는 메서드 (로그인 필요한 테스트의 경우 사용)
    public void getAuthentication(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("없는 아이디야!!!!")
        );
        UserDetails principal = new CustomUserDetails(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @Order(1)
    @DisplayName("createDelete: 로그인 후 게시물 좋아요 생성/삭제")
    public void createPostLoginLike(){
        // 회원가입, 로그인
        MemberRequestDto memberRequestDto = new MemberRequestDto("create@member1.com","asdfasdf","손윤환","http://profile.img", MemberGrade.TRAVELER);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
        getAuthentication(1L);
        // 포스트 작성
        List<String> hashtag = new ArrayList<>();
        hashtag.add("인천");
        hashtag.add("바다");
        hashtag.add("갈매기");
        PostRequestDto postRequestDto = new PostRequestDto("http://create.post", hashtag);
        postService.createPost(postRequestDto);
        // 좋아요 클릭
        ResponseDto responseDto = postLikeService.creatDeletePostLike(1L);
        Assertions.assertEquals(responseDto.getOk(),true);

        ResponseDto responseDto1 = postService.readPost(1L);
        Assertions.assertTrue(((DetailResponseDto) responseDto1.getResults()).getMember().isLike());

        // 좋아요 삭제
        ResponseDto responseDto2 = postLikeService.creatDeletePostLike(1L);
        Assertions.assertEquals(responseDto2.getOk(),true);

        ResponseDto responseDto3 = postService.readPost(1L);
        Assertions.assertFalse(((DetailResponseDto) responseDto3.getResults()).getMember().isLike());

        System.out.println(responseDto.getMsg());
        System.out.println(responseDto2.getMsg());
    }

    @Test
    @Order(2)
    @DisplayName("create: 비로그인 게시물 좋아요 생성/삭제")
    public void createPostNotLoginLike(){
        // 좋아요 클릭
        ResponseDto responseDto = postLikeService.creatDeletePostLike(1L);
        Assertions.assertEquals(responseDto.getOk(),false);

        ResponseDto responseDto1 = postService.readPost(1L);
        Assertions.assertFalse(((DetailResponseDto) responseDto1.getResults()).getMember().isLike());

        // 좋아요 삭제
        ResponseDto responseDto2 = postLikeService.creatDeletePostLike(1L);
        Assertions.assertEquals(responseDto2.getOk(),false);

        ResponseDto responseDto3 = postService.readPost(1L);
        Assertions.assertFalse(((DetailResponseDto) responseDto3.getResults()).getMember().isLike());

        System.out.println(responseDto.getMsg());
        System.out.println(responseDto2.getMsg());
    }

}
