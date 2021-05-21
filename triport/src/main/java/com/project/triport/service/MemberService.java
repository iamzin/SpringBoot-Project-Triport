package com.project.triport.service;

import com.project.triport.entity.Board;
import com.project.triport.entity.Member;
import com.project.triport.entity.MemberGradeUp;
import com.project.triport.repository.BoardRepository;
import com.project.triport.repository.MemberGradeUpRepository;
import com.project.triport.repository.MemberRepository;
import com.project.triport.requestDto.MemberInfoRequestDto;
import com.project.triport.responseDto.results.property.information.MemberInformationResponseDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.project.triport.entity.MemberGrade.TRAVEL_EDITOR;
import static com.project.triport.entity.MemberGrade.TRAVEL_MASTER;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final MemberGradeUpRepository memberGradeUpRepository;
    private final S3ProfileImageService s3ProfileImageService;
    private final PasswordEncoder passwordEncoder;

    // 현재 로그인한 member(SecurityContext에 있는 member)의 email로
    // member 전체 정보 조회
    @Transactional(readOnly = true)
    public ResponseDto getMember() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보가 없습니다."));
        MemberInformationResponseDto.of(member);
        return new ResponseDto(true, member, "로그인한 사용자의 프로필 조회에 성공하였습니다.");
    }

    // member 프로필 수정
    @Transactional
    public ResponseDto updateMember(MemberInfoRequestDto memberInfoRequestDto) throws IOException {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다.")
        );

        MultipartFile profileImgFile = memberInfoRequestDto.getProfileImgFile();
        String nickname = memberInfoRequestDto.getNickname();
        String password = passwordEncoder.encode(memberInfoRequestDto.getNewPassword());
        String passwordCheck = passwordEncoder.encode((memberInfoRequestDto.getNewPasswordCheck()));

        // 모든 항목 변경사항 없을 때
        if (profileImgFile.isEmpty() && nickname.equals(member.getNickname())
                && password.isEmpty() && passwordCheck.isEmpty()) {
            throw new RuntimeException("변경사항이 없습니다.");
        }
        // 프로필 이미지만 변경사항 없을 때
        else if (profileImgFile.isEmpty() && !nickname.equals(member.getNickname())
                && !password.isEmpty() && !passwordCheck.isEmpty()) {
            String fileUrl = member.getProfileImgUrl();
            member.updateMember(memberInfoRequestDto, password, fileUrl);
            return new ResponseDto(true, "프로필 수정이 완료되었습니다.");
        }
        // 비밀번호만 변경사항 없을 때
        else if (password.isEmpty() && passwordCheck.isEmpty()) {
            String newPassword = member.getPassword();
            String fileUrl = s3ProfileImageService.getFileUrl(profileImgFile);
            member.updateMember(memberInfoRequestDto, newPassword, fileUrl);
            return new ResponseDto(true, "프로필 수정이 완료되었습니다.");
        } else if (password.isEmpty() || passwordCheck.isEmpty()) {
            throw new RuntimeException("비밀번호와 비밀번호 확인을 모두 입력해 주세요.");
        }

        // 닉네임만 변경사항 없을 때 -> 프론트로부터 받은 값 (== 기존 nickname) 그대로 update


//        MultipartFile profileImgFile = memberRequestDto.getProfileImgFile();
//        String filePath = s3ProfileImageService.uploadProfileImage(memberInfoRequestDto.getProfileImgFile());
//        String fileUrl = "https://" + S3ProfileImageService.CLOUD_FRONT_DOMAIN_NAME + "/profileImage/" + filePath;

        String fileUrl = s3ProfileImageService.getFileUrl(profileImgFile);
        member.updateMember(memberInfoRequestDto, password, fileUrl);

        return new ResponseDto(true, "특정 member의 프로필 수정에 성공하였습니다.");
    }

    // member 삭제(탈퇴)
    public ResponseDto deleteMember() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다."));

        memberRepository.delete(member);
        return new ResponseDto(true, "회원탈퇴가 완료되었습니다.");
    }

    // member grade up
    // TRAVELER mailing: Trils 좋아요 5개 이상
    // TRAVELER -> TRAVEL EDITOR: Trilog 1개 이상 create
    // TRAVEL EDITOR -> TRAVEL MASTER: Trilog 3개 이상 create
    public void GradeupMember(Member member) {
        List<Board> boardList = boardRepository.findByMember(member);
        Long boardNum = (long) boardList.size();

        MemberGradeUp memberGradeUp = memberGradeUpRepository.findByMember(member);

        if (boardNum == 1) {
            memberGradeUp.gradeUpMember(member, TRAVEL_EDITOR);
        } else if (boardNum == 3) {
            memberGradeUp.gradeUpMember(member, TRAVEL_MASTER);
        }

        // TODO: triport 사이트 알림, 메일링 메소드 추가

    }
}
