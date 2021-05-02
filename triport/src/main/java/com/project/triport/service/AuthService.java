package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.RefreshToken;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.jwt.TokenProvider;
import com.project.triport.repository.MemberRepository;
import com.project.triport.repository.RefreshTokenRepository;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.requestDto.TokenRequestDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.responseDto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail()))
            throw new RuntimeException("이미 가입되어 있는 email 입니다.");

        Member member = memberRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.of(memberRepository.save(member));
    }

    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        // 1. Login 시 입력한 ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(memberRequestDto.getEmail(),memberRequestDto.getPassword());

        // 2. 실제로 비밀번호 검증이 이루어지는 부분
        //    authenticate method가 실행될 때, CustomuserDetailService에서 만들었던 loadUserByUsername method 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // 3. 인증 정보 authentication을 기반으로 JWT token 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. token 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Access Token에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. Refresh Token 저장소에서 Member ID를 기반으로 Refresh Token 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token이 일치하는지 확인
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Token의 user 정보가 일치하지 않습나다."); // Refresh Token이 일치하지 않습니다.
        }

        // 5. new token 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. Refresh Token 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 7. token 발급
        return tokenDto;
    }
}
