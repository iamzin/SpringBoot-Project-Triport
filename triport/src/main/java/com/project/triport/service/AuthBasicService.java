package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGradeUp;
import com.project.triport.entity.MemberPromotion;
import com.project.triport.entity.RefreshToken;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.jwt.TokenProvider;
import com.project.triport.repository.MemberGradeUpRepository;
import com.project.triport.repository.MemberPromotionRepository;
import com.project.triport.repository.MemberRepository;
import com.project.triport.repository.RefreshTokenRepository;
import com.project.triport.requestDto.AuthLoginReqeustDto;
import com.project.triport.requestDto.MemberInfoRequestDto;
import com.project.triport.requestDto.TokenRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.responseDto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static com.project.triport.responseDto.results.property.information.MemberInformationResponseDto.of;

@Service
@RequiredArgsConstructor
public class AuthBasicService {

    private final MemberRepository memberRepository;
    private final MemberGradeUpRepository memberGradeUpRepository;
    private final MemberPromotionRepository memberPromotionRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 기본 회원가입
    @Transactional
    public ResponseDto signup(MemberInfoRequestDto memberInfoRequestDto) {
        Member member = memberRepository.findByEmail(memberInfoRequestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("이미 가입되어 있는 email 입니다.")
        );

        if (memberRepository.existsByNickname(memberInfoRequestDto.getNickname())) {
            return new ResponseDto(false, "이미 존재하는 nickname 입니다.", 400);
        }

        member.toMember(memberInfoRequestDto, passwordEncoder);
        of(memberRepository.save(member));

        // grade 관리를 위해 가입과 동시에 grade up table에 추가
        MemberGradeUp memberGradeUp = new MemberGradeUp().newMemberGrade(member);
        memberGradeUpRepository.save(memberGradeUp);

        // promotion Mail history 관리를 위해 가입과 동시에 member promotion history table에 추가
        MemberPromotion memberPromotion = new MemberPromotion().newMemberPromo(member);
        memberPromotionRepository.save(memberPromotion);

        return new ResponseDto(true, "회원가입 성공하였습니다.", 200);
    }

    // 기본 로그인
    @Transactional
    public ResponseDto login(AuthLoginReqeustDto authLoginReqeustDto, HttpServletResponse response) {
        // 0. Member 존재 여부 확인
        memberRepository.findByEmail(authLoginReqeustDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 사용자를 찾을 수 없습니다.")
        );

        // 1. Login 시 입력한 ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authLoginReqeustDto.getEmail(), authLoginReqeustDto.getPassword());

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
    public ResponseDto reissue(TokenRequestDto tokenRequestDto, HttpServletResponse response) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("회원 정보가 유효하지 않습니다. 관리자에게 문의하세요.");
        }

        // 2. Access Token에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. Refresh Token 저장소에서 Member ID를 기반으로 Refresh Token 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 유효하지 않습니다. 관리자에게 문의하세요.")); // TODO: front로 로그인이 필요함을 return

        // 4. Refresh Token이 일치하는지 확인
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new IllegalArgumentException("회원 정보가 일치하지 않습나다. 관리자에게 문의하세요."); // Refresh Token이 일치하지 않습니다.
        }

        // 5. new token 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. Refresh Token 저장소 정보 업데이트
        refreshToken.updateValue(tokenDto.getRefreshToken());

        // 7. header에 token 담고, memberInfo return
        return tokenToHeaders(authentication, tokenDto, response);
    }

    public ResponseDto logout() {
        Member member = getAuthMember();
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(member.getEmail())
                .orElseThrow( () -> new IllegalArgumentException("회원 정보가 유효하지 않습니다. 관리자에게 문의하세요.")
        );
        refreshTokenRepository.delete(refreshToken);
        return new ResponseDto(true, "로그아웃이 완료되었습니다.", 200);
    }

    // access, refresh token header에 담기
    public ResponseDto tokenToHeaders(Authentication authentication, TokenDto tokenDto, HttpServletResponse response) {

        // Header에 token과 만료시간 add
        response.addHeader("Access-Token", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", "Bearer " + tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());

        // 해당 memberInfo 담기
        Member member = memberRepository.findMemberByEmail(authentication.getName());

        // ResponseBody에 memberInfo 담아서 return
        return new ResponseDto(true, member, "로그인 되었습니다.", 200);
    }

    public Member getAuthMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getMember();
    }
}
