package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.RefreshToken;
import com.project.triport.jwt.TokenProvider;
import com.project.triport.kakao.KakaoOAuth2;
import com.project.triport.kakao.KakaoUserInfo;
import com.project.triport.repository.MemberRepository;
import com.project.triport.repository.RefreshTokenRepository;
import com.project.triport.responseDto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthKakaoService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthBasicService authBasicService;
    private final MemberRepository memberRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private final @Value("${kakao.secret}") String kakaoKey;

    // Kakao 로그인
    public void kakaoLogin(String authorizedCode, HttpServletResponse response) {
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
            // Kakao email과 동일한 email의 Member 있는지 확인
            Member sameEmailMember = memberRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailMember != null) {
                kakaoUser = sameEmailMember;
                // Kakao email과 동일한 email의 회원이 있는 경우
                // KakaoId 필드 업데이트
                kakaoUser.updateKakoId(kakaoId);
            } else {
                // Kakao 정보로 회원가입
                // Member email == Kakao email
                String email = kakaoEmail;
                // Member password == kakaoId + kakaoKey
                String password = kakaoId + kakaoKey;
                // Member nickname == kakao nickname
                String nickname = kakaoNickname;
                // Member profileImgUrl == kakao profile_image_url
                String profileImgUrl = kakaoProfileImgUrl;

                kakaoUser = kakaoUser.KakaoLoginMember(kakaoId, email, password, nickname, profileImgUrl);
                memberRepository.save(kakaoUser);
            }
        }

        // 강제 로그인 처리
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(kakaoUser.getEmail(), kakaoUser.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .email(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        authBasicService.tokenToHeaders(authentication, tokenDto, response);

    }
}
