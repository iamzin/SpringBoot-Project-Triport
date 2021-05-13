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
import com.project.triport.responseDto.results.property.information.MemberInformationResponseDto;
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
    private final AuthBasicService authBasicService;
    private final MemberRepository memberRepository;
    private final KakaoOAuth2 kakaoOAuth2;
    private @Value("${kakao.secret}")
    String kakaoKey;

    // Kakao 로그인
    @Transactional
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

                kakaoUser.KakaoLoginMember(kakaoId, email, password, nickname, profileImgUrl);
                memberRepository.save(kakaoUser);
            }
        }

        // 강제 로그인 처리
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(kakaoUser.getAuthority().toString());
        UserDetails principal = new org.springframework.security.core.userdetails.User(
                String.valueOf(kakaoUser.getId()),
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
//        return authBasicService.tokenToHeaders(authentication, tokenDto, response);

        // Header에 token과 만료시간 add
        response.addHeader("Access-Token", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", "Bearer " + tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        // react가 만료시간 체크 하는 방법:
        // accessToken 만료하기 1분 전에 로그인 연장하도록 아래와 같이 setTimeout 설정
        // setTimeout(onSilentRefresh, JWT_EXPIRRY_TIME - 60000);
        // Timeout 되면 onSilentRefresh가 실행되면서, /auth/reissue로 재발급 요청

        // 해당 memberInfo return

        // ResponseBody에 memberInfo 담아서 return
        new MemberInformationResponseDto(kakaoUser);
        return new ResponseDto(true, kakaoUser, "사용자 token 발급을 성공하였습니다.");
    }
}
