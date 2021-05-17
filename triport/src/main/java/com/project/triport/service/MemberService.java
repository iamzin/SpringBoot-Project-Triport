package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.repository.MemberRepository;
import com.project.triport.requestDto.MemberRequestDto;
import com.project.triport.responseDto.MemberResponseDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ProfileImageService s3ProfileImageService;

    // 현재 로그인한 member(SecurityContext에 있는 member)의 email로
    // member 전체 정보 조회
    @Transactional(readOnly = true)
    public ResponseDto getMember() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보가 없습니다."));
        MemberResponseDto.of(member);
        return new ResponseDto(true, member, "로그인한 사용자의 프로필 조회에 성공하였습니다.");
    }

    // member 프로필 수정
    @Transactional
    public ResponseDto updateMember(MemberRequestDto memberRequestDto) throws IOException {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다.")
        );

        // 프로필 이미지 변경사항 없을 때
        if (memberRequestDto.getProfileImgFile().isEmpty()) {
            // 기본 이미지 유지
            // no action
        }

        MultipartFile profileImgFile = memberRequestDto.getProfileImgFile();
        String fileName = s3ProfileImageService.uploadProfileImage(profileImgFile);
        String filePath = "https://" + S3ImageService.CLOUD_FRONT_DOMAIN_NAME + "/image/" + fileName;

        member.updateMember(memberRequestDto, passwordEncoder, filePath);
        return new ResponseDto(true, "특정 member의 프로필 수정에 성공하였습니다.");
    }

    // member 삭제(탈퇴)
    public String deleteMember() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다."));

        memberRepository.delete(member);
        return "회원탈퇴가 완료되었습니다.";
    }
}
