package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGradeUp;
import com.project.triport.entity.MemberPromotion;
import com.project.triport.entity.RefreshToken;
import com.project.triport.jwt.TokenProvider;
import com.project.triport.kakao.KakaoOAuth2;
import com.project.triport.kakao.KakaoUserInfo;
import com.project.triport.repository.MemberGradeUpRepository;
import com.project.triport.repository.MemberPromotionRepository;
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
    private final MemberRepository memberRepository;
    private final MemberGradeUpRepository memberGradeUpRepository;
    private final MemberPromotionRepository memberPromotionRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthBasicService authBasicService;
    private final KakaoOAuth2 kakaoOAuth2;
    private @Value("${kakao.secret}") String kakaoKey;
    private final TokenProvider tokenProvider;

    // Kakao 로그인
//    @Transactional
    public ResponseDto kakaoLogin(String authorizedCode, HttpServletResponse response) {

        // if email이 empty 값일 경우 반환? 이 단계까지 안 올 듯

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

                // grade 관리를 위해 가입과 동시에 grade up table에 추가
                MemberGradeUp memberGradeUp = new MemberGradeUp().newMemberGrade(kakaoUser);
                memberGradeUpRepository.save(memberGradeUp);

                // promotion Mail history 관리를 위해 가입과 동시에 member promotion history table에 추가
                MemberPromotion memberPromotion = new MemberPromotion().newMemberPromo(kakaoUser);
                memberPromotionRepository.save(memberPromotion);
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

    // TODO: 회원 탈퇴 시, 카카오와 서비스 연결 끊기
//    public void disconnectKakaoAndMember() {
//        response.
//    }
}
