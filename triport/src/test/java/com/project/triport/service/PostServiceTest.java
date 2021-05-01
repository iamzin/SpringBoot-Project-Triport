package com.project.triport.service;

import com.project.triport.entity.Authority;
import com.project.triport.entity.Member;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.MemberRepository;
import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.responseDto.ResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PostServiceTest{

    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;

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
    @DisplayName("Create:비로그인 유저 작성 불가")
    public void createPostNotLoginTest(){
        List<String> hashtag = new ArrayList<>();
        hashtag.add("인천");
        hashtag.add("바다");
        hashtag.add("갈매기");
        PostRequestDto postRequestDto = new PostRequestDto("http://create.post", hashtag);
        ResponseDto responseDto = postService.createPost(postRequestDto);
        System.out.println(responseDto.getMsg());
        Assertions.assertThat(responseDto.getOk()).isEqualTo(false);
    }

    @Test
    @DisplayName("create:로그인(인증) 유저 작성")
    public void createPostLoginTest(){
        Member member = new Member("create@member1.com","asdfasdf",Authority.ROLE_USER);
        memberRepository.save(member);

        getAuthentication(1L);

        List<String> hashtag = new ArrayList<>();
        hashtag.add("인천");
        hashtag.add("바다");
        hashtag.add("갈매기");
        PostRequestDto postRequestDto = new PostRequestDto("http://create.post", hashtag);
        ResponseDto responseDto = postService.createPost(postRequestDto);
        System.out.println(responseDto.getMsg());
        Assertions.assertThat(responseDto.getOk()).isEqualTo(true);
    }

    @Test
    @DisplayName("read:10개 작성 후 List 불러오기")
    public void readPostAllTest(){

        getAuthentication(1L);

        for(int i = 0; i < 10; i++) {
            List<String> hashtag = new ArrayList<>();
            hashtag.add("인천"+i);
            hashtag.add("바다"+i);
            hashtag.add("갈매기"+i);
            PostRequestDto postRequestDto = new PostRequestDto("http://create.post"+i, hashtag);
            ResponseDto responseDto = postService.createPost(postRequestDto);
            System.out.println(responseDto.getMsg());
        }
        ResponseDto responseDto = postService.readPostsAll(1, "modifiedAt");
//        System.out.println(((List<?>)responseDto.getResults()).size());
        Assertions.assertThat(((List<?>)responseDto.getResults()).size()).isEqualTo(10);
    }

    @Test
    @DisplayName("read:1개 작성 후 Post Detail 불러오기")
    public void readPostTest(){

    }





}
