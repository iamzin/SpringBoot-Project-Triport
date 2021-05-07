package com.project.triport.service;

import com.project.triport.entity.Authority;
import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGrade;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.MemberRepository;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.requestDto.PostRequestDto;
import com.project.triport.responseDto.ResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceTest{
    // 메서드 하나씩 확인하려면 앞서 회원가입 절차가 필요한 녀석들이 있어서 전체 클래스를 한 번에 테스트 하는 방식으로 구현하였다.

    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    @DisplayName("create:로그인(인증) 유저 작성")
    public void createPostLoginTest(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("create@member1.com","asdfasdf","박은진","http://profile.img",MemberGrade.TRAVELER);
        Member member = memberRequestDto.toMember(passwordEncoder);
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
    @Order(2)
    @DisplayName("전체 List, 검색 List 불러오기")
    public void readPostAllTest(){

        getAuthentication(1L);

        for(int i = 0; i < 5; i++) {
            List<String> hashtag = new ArrayList<>();
            hashtag.add("인천");
            hashtag.add("바다"+i);
            hashtag.add("갈매기"+i);
            PostRequestDto postRequestDto = new PostRequestDto("http://create.post"+i, hashtag);
            ResponseDto responseDto = postService.createPost(postRequestDto);
            System.out.println(responseDto.getMsg());
        }
        for(int i = 0; i < 5; i++) {
            List<String> hashtag = new ArrayList<>();
            hashtag.add("춘천");
            hashtag.add("바다");
            hashtag.add("갈매기"+i);
            PostRequestDto postRequestDto = new PostRequestDto("http://create.post"+i, hashtag);
            ResponseDto responseDto = postService.createPost(postRequestDto);
            System.out.println(responseDto.getMsg());
        }

        ResponseDto responseDto = postService.readPostsAll(1, "modifiedAt","");
        Assertions.assertThat(((List<?>)responseDto.getResults()).size()).isEqualTo(10);
//        ResponseDto responseDtoSearch = postService.readPostsAll(1, "modifiedAt","춘천");
//        Assertions.assertThat(((List<?>)responseDtoSearch.getResults()).size()).isEqualTo(5);
        ResponseDto responseDtoSearchContaining = postService.readPostsAll(1, "modifiedAt","춘천");
        Assertions.assertThat(((List<?>)responseDtoSearchContaining.getResults()).size()).isEqualTo(5);
    }

    @Test
    @Order(3)
    @DisplayName("read:Post Detail 불러오기")
    public void readPostTest(){
        ResponseDto responseDtoTrue = postService.readPost(1L);
        Assertions.assertThat(responseDtoTrue.getOk()).isEqualTo(true);
    }

    @Test
    @Order(4)
    @DisplayName("read:신규 가입 후 본인 메세지만 불러오기")
    public void readPostsMemberTest(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("create@member2.com","asdfasdf","채진욱","http://profile.img",MemberGrade.TRAVELER);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
        Long memberId = memberRepository.findByEmail("create@member2.com").orElseThrow(
                () -> new IllegalArgumentException("없는 아이디야!!!")
        ).getId();

        getAuthentication(memberId);
        for(int i = 0; i < 3; i++) {
            List<String> hashtag = new ArrayList<>();
            hashtag.add("일산");
            hashtag.add("공원");
            hashtag.add("푸릇푸릇");
            PostRequestDto postRequestDto = new PostRequestDto("http://create.post", hashtag);
            postService.createPost(postRequestDto);
        }
        ResponseDto responseDto = postService.readPostsMember();
        Assertions.assertThat(responseDto.getOk()).isEqualTo(true);
        Assertions.assertThat(((List<?>)responseDto.getResults()).size()).isEqualTo(3);
    }

    @Test
    @Order(5)
    @DisplayName("update:로그인 후 포스트 수정하기")
    public void updatePostLoginTest(){
        getAuthentication(1L);
        List<String> hashtag = new ArrayList<>();
        hashtag.add("수정");
        hashtag.add("로그인");
        hashtag.add("목아파");
        PostRequestDto postRequestDto = new PostRequestDto("http://update.post.setAuth", hashtag);
        ResponseDto responseDto = postService.updatePost(postRequestDto,1L);
        Assertions.assertThat(responseDto.getOk()).isEqualTo(true);
    }

//    @Test
//    @Order(7)
//    @DisplayName("update:비로그인 포스트 수정하기")
//    public void updatePostNotLoginTest(){
//        List<String> hashtag = new ArrayList<>();
//        hashtag.add("수정");
//        hashtag.add("비로그인");
//        hashtag.add("푸릇푸릇");
//        PostRequestDto postRequestDto = new PostRequestDto("http://create.post.removeAuth", hashtag);
//        ResponseDto responseDto = postService.updatePost(postRequestDto,1L);
//        Assertions.assertThat(responseDto.getOk()).isEqualTo(false);
//    }

    @Test
    @Order(6)
    @DisplayName("delete:로그인 후 포스트 삭제하기")
    public void deletePostLoginTest(){
        getAuthentication(1L);
        ResponseDto responseDto = postService.deletePost(1L);
        Assertions.assertThat(responseDto.getOk()).isEqualTo(true);
    }

//    @Test
//    @Order(1)
//    @DisplayName("create:비로그인 유저 작성 불가")
//    public void createPostNotLoginTest(){
//        List<String> hashtag = new ArrayList<>();
//        hashtag.add("인천");
//        hashtag.add("바다");
//        hashtag.add("갈매기");
//        PostRequestDto postRequestDto = new PostRequestDto("http://create.post", hashtag);
//        ResponseDto responseDto = postService.createPost(postRequestDto);
//        System.out.println(responseDto.getMsg());
//        Assertions.assertThat(responseDto.getOk()).isEqualTo(false);
//    }

//    @Test
//    @Order(9)
//    @DisplayName("delete:비로그인 포스트 삭제하기")
//    public void deletePostNotLoginTest(){
//        ResponseDto responseDto = postService.deletePost(2L);
//        Assertions.assertThat(responseDto.getOk()).isEqualTo(false);
//    }

}
