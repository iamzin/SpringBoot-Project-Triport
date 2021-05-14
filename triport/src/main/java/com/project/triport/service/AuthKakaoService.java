package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.RefreshToken;
import com.project.triport.jwt.TokenProvider;
import com.project.triport.kakao.KakaoOAuth2;
import com.project.triport.kakao.KakaoUserInfo;
import com.project.triport.repository.MemberRepository;
import com.project.triport.repository.RefreshTokenRepository;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthKakaoService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final AuthBasicService authBasicService;
    private @Value("${kakao.secret}") String kakaoKey;

    // Kakao 로그인
//    @Transactional
    public ResponseDto kakaoLogin(String authorizedCode, HttpServletResponse response) {
        // Kakao OAuth2를 통해 Kako 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        System.out.println("userInfo = " + userInfo);
        Long kakaoId = userInfo.getId();
        System.out.println("kakaoId = " + kakaoId);
        String kakaoEmail = userInfo.getEmail();
        System.out.println("kakaoEmail = " + kakaoEmail);
        String kakaoNickname = userInfo.getNickname();
        System.out.println("kakaoNickname = " + kakaoNickname);
        String kakaoProfileImgUrl = userInfo.getProfileImgUrl();
        System.out.println("kakaoProfileImgUrl = " + kakaoProfileImgUrl);

        // DB에 중복된 Kakao Id가 있는지 확인
        Member kakaoUser = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);
        System.out.println("kakaoUser = " + kakaoUser);

        if (kakaoUser == null) {
            Member sameMember = null;
            if (memberRepository.existsByEmail(kakaoEmail)) {
                sameMember = memberRepository.findByEmail(kakaoEmail).orElseThrow(
                        () -> new IllegalArgumentException("kakaoMember email을 찾을 수 없습니다.")
                );
                System.out.println("sameMember = " + sameMember);
                kakaoUser = sameMember;
                System.out.println("kakaoUser = " + kakaoUser);
                kakaoUser.updateKakoId(kakaoId);
                System.out.println("kakaoUser = " + kakaoUser);
            } else {
                // Kakao 정보로 회원가입
                // Member password == kakaoId + kakaoKey
                String password = kakaoId + kakaoKey;
                System.out.println("password = " + password);
                kakaoUser = new Member().KakaoLoginMember(kakaoId, kakaoEmail, password, kakaoNickname, kakaoProfileImgUrl);
                System.out.println("password = " + password);
                memberRepository.save(kakaoUser);
                System.out.println("password = " + password);
            }
        }

        // 강제 로그인 처리
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(kakaoUser.getAuthority().toString());
        System.out.println("grantedAuthority = " + grantedAuthority);
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                String.valueOf(kakaoUser.getEmail()),
                kakaoUser.getPassword(),
                Collections.singleton(grantedAuthority));
        System.out.println("principal = " + principal);

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        System.out.println("authentication = " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        System.out.println("tokenDto = " + tokenDto);

        RefreshToken refreshToken = RefreshToken.builder()
                .email(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        System.out.println("refreshToken = " + refreshToken);

        refreshTokenRepository.save(refreshToken);
        System.out.println("refreshToken = " + refreshToken);

        return authBasicService.tokenToHeaders(authentication, tokenDto, response);
    }
}
