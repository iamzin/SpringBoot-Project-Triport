package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.RefreshToken;
import com.project.triport.jwt.TokenProvider;
import com.project.triport.repository.MemberRepository;
import com.project.triport.repository.RefreshTokenRepository;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.requestDto.TokenRequestDto;
import com.project.triport.responseDto.MemberInfoResponseDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.responseDto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class AuthBasicService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 기본 회원가입
    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail()))
            throw new RuntimeException("이미 가입되어 있는 email 입니다.");

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    // 기본 로그인
    @Transactional
    public MemberInfoResponseDto login(MemberRequestDto memberRequestDto, HttpServletResponse response) {
        // 1. Login 시 입력한 ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberRequestDto.getEmail(), memberRequestDto.getPassword());

        // 2. 실제로 비밀번호 검증이 이루어지는 부분
        //    authenticate method가 실행될 때, CustomuserDetailService에서 만들었던 loadUserByUsername method 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보 authentication을 기반으로 JWT token 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .email(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. header에 token 담고, memberInfo return
        return tokenToHeaders(authentication, tokenDto, response);
    }

    // 기본 access, refresh token 재발급
    @Transactional
    public MemberInfoResponseDto reissue(TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Access Token에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. Refresh Token 저장소에서 Member ID를 기반으로 Refresh Token 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다.")); // TODO: front로 로그인이 필요함을 return

        // 4. Refresh Token이 일치하는지 확인
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Token의 user 정보가 일치하지 않습나다."); // Refresh Token이 일치하지 않습니다.
        }

        // 5. new token 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. Refresh Token 저장소 정보 업데이트
        refreshToken.updateValue(tokenDto.getRefreshToken());

        // 7. header에 token 담고, memberInfo return
        return tokenToHeaders(authentication, tokenDto, response);
    }

    // access, refresh token header에 담기
    public MemberInfoResponseDto tokenToHeaders(Authentication authentication,
                                                TokenDto tokenDto, HttpServletResponse response) {

        // Header에 token과 만료시간 add
        response.addHeader("Access-Token", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", "Bearer " + tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        // react가 만료시간 체크 하는 방법:
        // accessToken 만료하기 1분 전에 로그인 연장하도록 아래와 같이 setTimeout 설정
        // setTimeout(onSilentRefresh, JWT_EXPIRRY_TIME - 60000);
        // Timeout 되면 onSilentRefresh가 실행되면서, /auth/reissue로 재발급 요청

        // 해당 memberInfo return
        Member member = memberRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다.")
        );

        // ResponseBody에 memberInfo 담아서 return
        return new MemberInfoResponseDto(member.getId(), member.getNickname());
    }
}
