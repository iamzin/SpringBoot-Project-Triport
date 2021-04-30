package com.project.triport.service;

import com.project.triport.repository.MemberRepository;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true) // transaction.annotation을 import 해야 함
    public MemberResponseDto getMemberInfo(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));
    }

    // 로그인한 user(member) 정보 가져오기
    // 현재 SecurityContext에 있는 user(member) 정보 가져오기
    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo() { // getMemberInfo()
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보가 없습니다."));
    }
}
