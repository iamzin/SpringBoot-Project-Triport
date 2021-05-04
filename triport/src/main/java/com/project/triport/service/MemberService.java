package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.repository.MemberRepository;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
//    private final org.slf4j.Logger logger;

    // 로그인한 member email 가져오기
    // 현재 SecurityContext에 있는 member email 가져오기
    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo() {
        return memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보가 없습니다."));
    }

    public MemberResponseDto updateMemberInfo(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다."));

        member.update(memberRequestDto);
        return MemberResponseDto.of(member);
    }

    public String deleteMember() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다."));

        memberRepository.delete(member);
        return "회원탈퇴가 완료되었습니다.";
    }

    public String updatePwd(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다."));

        member.updatePassword(memberRequestDto);
        return "비밀번호 변경이 완료되었습니다.";
    }

//    public String sendTempPwd() {
//
//        // Java가 제공하는 랜덤 객체 선언
//        Random random = new Random();
//
//        // random.nextInt() 함수로 난수 생성
//        // checkNum에 난수 생성 결과값 할당
//        // 111111~999999 범위의 숫자를 얻기 위해 nextInt(888888) + 111111
//        int checkNum = random.nextInt(888888) + 111111;
//
//        logger.info("난수 생성: " + checkNum);
//
//
//
//    }
}
