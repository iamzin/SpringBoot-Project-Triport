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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthKakaoService {
    private final KakaoOAuth2 kakaoOAuth2;
    private @Value("${kakao.secret}") String kakaoKey;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthBasicService authBasicService;

    // Kakao 로그인
//    @Transactional
    public ResponseDto kakaoLogin(String authorizedCode, HttpServletResponse response) {
        // Kakao OAuth2를 통해 Kako 사용자 정보 조회
        KakaoUserInfo userInfo = kakaoOAuth2.getUserInfo(authorizedCode);
        Long kakaoId = userInfo.getId();
        String kakaoEmail = userInfo.getEmail();
        String kakaoNickname = userInfo.getNickname();
        String kakaoProfileImgUrl = userInfo.getProfileImgUrl();

        // DB에 중복된 Kakao Id가 있는지 확인
        Member kakaoUser = memberRepository.findByKakaoId(kakaoId)
                .orElse(null);

        if (kakaoUser == null) {
            Member sameMember = null;
            if (memberRepository.existsByEmail(kakaoEmail)) {
                sameMember = memberRepository.findByEmail(kakaoEmail).orElseThrow(
                        () -> new IllegalArgumentException("kakaoMember email을 찾을 수 없습니다.")
                );
                kakaoUser = sameMember;
                kakaoUser.updateKakoId(kakaoId);
            } else {
                // Kakao 정보로 회원가입
                // Member password == kakaoId + kakaoKey
                String password = kakaoId + kakaoKey;
                kakaoUser = new Member().KakaoLoginMember(kakaoId, kakaoEmail, password, kakaoNickname, kakaoProfileImgUrl);
                memberRepository.save(kakaoUser);
            }
        }

        // 강제 로그인 처리
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(kakaoUser.getAuthority().toString());
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                String.valueOf(kakaoUser.getEmail()),
                kakaoUser.getPassword(),
                Collections.singleton(grantedAuthority));

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .email(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return authBasicService.tokenToHeaders(authentication, tokenDto, response);
    }
}
