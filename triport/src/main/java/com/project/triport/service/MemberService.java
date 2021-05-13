package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.repository.MemberRepository;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인한 member email 가져오기
    // 현재 SecurityContext에 있는 member email 가져오기
    @Transactional(readOnly = true)
    public MemberResponseDto getMember() {
        return memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .map(MemberResponseDto::of) // TODO: map 장단점 확인
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보가 없습니다."));
        // memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
        //      .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보가 없습니다."));
        // return MemberResponseDto.of(member);
    }

    public MemberResponseDto updateMember(MemberRequestDto memberRequestDto) {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다.")
        );

        member.updateMember(memberRequestDto, passwordEncoder);
        return MemberResponseDto.of(member);
    }

    public String deleteMember() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다."));

        memberRepository.delete(member);
        return "회원탈퇴가 완료되었습니다.";
    }
}
