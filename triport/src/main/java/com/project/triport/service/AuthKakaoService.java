//package com.project.triport.service;
//
//import com.project.triport.entity.Member;
//import com.project.triport.jwt.TokenProvider;
//import com.project.triport.repository.MemberRepository;
//import com.project.triport.repository.RefreshTokenRepository;
//import com.project.triport.requestDto.MemberRequestDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthKakaoService {
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//    private final MemberRepository memberRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenRepository refreshTokenRepository;
//    private final KakoOAuth2 kakaoOAuth2;
//    private final @Value("${kakao.secret}") String kakaoKey;
//
//    public void kakaoLogin(String authorizedCode) {
//        // Kakao OAuth2를 통해 Kako 사용자 정보 조회
//        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
//        Long kakaoId = userInfo.getId();
//        String email = userInfo.getEmail();
//        String nickname = userInfo.getNickname();
//
//        // Member email == Kako nickname
//        String email = email;
//        // Member password == kakaoId + kakaKey
//        String rawPassword = kakaoId + kakaoKey;
//        // Member nickname == kakao nickname
//        String nickname = nickname;
//
//        // DB에 중복된 Kakao Id가 있는지 확인
//        Member kakaoUser = memberRepository.findByKakaoId(kakaoId)
//                .orElse(null);
//
//        // Kakao 정보로 회원가입
//        if (kakaoUser == null) {
//            // password encoding
//            String password = passwordEncoder.encode(rawPassword);
//            kakaoUser = new Member.fromKakaoMember(kakaoId, email, password, nickname);
//        }
//
//
//        return "";
//    }
//}
