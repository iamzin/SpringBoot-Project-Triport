package com.project.triport.service;

import com.project.triport.entity.*;
import com.project.triport.repository.*;
import com.project.triport.requestDto.MemberImgRequestDto;
import com.project.triport.requestDto.MemberNicknameRequestDto;
import com.project.triport.requestDto.MemberPwdRequestDto;
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

import static com.project.triport.entity.MemberGrade.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberGradeUpRepository memberGradeUpRepository;
    private final MemberPromotionRepository memberPromotionRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentParentRepository commentParentRepository;
    private final CommentParentLikeRepository commentParentLikeRepository;
    private final CommentChildRepository commentChildRepository;
    private final CommentChildLikeRepository commentChildLikeRepository;
    private final S3ProfileImageService s3ProfileImageService;
    private final PasswordEncoder passwordEncoder;

    // 현재 로그인한 member(SecurityContext에 있는 member)의 email로
    // member 전체 정보 조회
    @Transactional(readOnly = true)
    public ResponseDto getMember() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보가 없습니다."));
        MemberInformationResponseDto.of(member);
        return new ResponseDto(true, member, "로그인한 사용자의 프로필 조회에 성공하였습니다.", 200);
    }

    @Transactional
    public ResponseDto updateMemberNickname(MemberNicknameRequestDto memberNicknameRequestDto) {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다.")
        );

        String nickname = memberNicknameRequestDto.getNickname();
        // 변경사항 없을 때
        if (nickname.equals(member.getNickname())) {
            return new ResponseDto(false, "변경사항이 없습니다.", 400);
        }
        // 변경사항 있을 때
        // 중복된 닉네임일 때
        else if (memberRepository.existsByNickname(nickname)) {
                return new ResponseDto(false, "이미 존재하는 nickname 입니다.", 400);
        }

        member.updateMemberNickname(nickname);
        return new ResponseDto(true, "닉네임 변경이 완료되었습니다.", 200);
    }

    @Transactional
    public ResponseDto updateMemberPwd(MemberPwdRequestDto memberPwdRequestDto) {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다.")
                );

        String newPassword = memberPwdRequestDto.getNewPassword();
        String newPasswordCheck = memberPwdRequestDto.getNewPasswordCheck();

        // 비밀번호, 비밀번호 확인 모두 빈 값일 때
        if (newPassword.isEmpty() && newPasswordCheck.isEmpty()) {
            return new ResponseDto(false, "변경사항이 없습니다.", 400);
        }
        // 비밀번호는 입력하고, 비밀번호 확인은 빈 값일 때 (비밀번호는 Entity에서 Valid로 검증됨)
        else if (!newPassword.isEmpty() && newPasswordCheck.isEmpty()) {
            return new ResponseDto(false, "비밀번호와 비밀번호 확인을 모두 입력해 주세요.", 400);
        }
        String encodeNewPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodeNewPassword);
        return new ResponseDto(true, "비밀번호 변경이 완료되었습니다.", 200);
    }

    @Transactional
    public ResponseDto updateMemberProfileImg(MemberImgRequestDto memberImgRequestDto) throws IOException {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다.")
                );

        MultipartFile profileImgFile = memberImgRequestDto.getProfileImgFile();
        String fileUrl = s3ProfileImageService.getFileUrl(profileImgFile);
        member.updateMemberProfileImg(fileUrl);

        return new ResponseDto(true, "프로필 이미지 수정이 완료되었습니다.", fileUrl, 200);
    }

    // member grade up
    // TRAVELER mailing: Trils 좋아요 5개 이상
    // TRAVELER -> TRAVEL EDITOR: Trilog 1개 이상 create
    // TRAVEL EDITOR -> TRAVEL MASTER: Trilog 3개 이상 create
    @Transactional
    public String GradeupMember(Member member) {
        List<Board> boardList = boardRepository.findByMember(member);
        Long boardNum = (long) boardList.size();

        MemberGradeUp memberGradeUp = memberGradeUpRepository.findByMember(member);

        final String subMsg = "no change";

        if (boardNum == 1) {
            memberGradeUp.gradeUpMember(member, TRAVEL_EDITOR);
            member.updateGrade(TRAVEL_EDITOR);
            return "TRAVEL Editor";
        } else if (boardNum == 3) {
            memberGradeUp.gradeUpMember(member, TRAVEL_MASTER);
            member.updateGrade(TRAVEL_MASTER);
            return "TRAVEL Master";
        }

        return subMsg;
    }

    // member 삭제(탈퇴)
    @Transactional
    public ResponseDto deleteMember() {
        Member member = memberRepository.findByEmail(SecurityUtil.getCurrentMemberEmail())
                .orElseThrow(() -> new IllegalArgumentException("로그인한 사용자 정보를 찾을 수 없습니다."));

        // TODO: kakaoId가 있는 member인 경우 AuthKakaoService를 통해 Kakao 연결끊기 실행
//        if (!(member.getKakaoId() == null)) {
//
//        }

        // post
        // postLike minus 처리
        List<PostLike> postLikeList = postLikeRepository.findByMember(member);
        for (PostLike postLike : postLikeList) {
            postLike.getPost().minusLikeNum();
        }
        // postLike 삭제: 해당 member가 postLike한 기록 삭제
        postLikeRepository.deleteAllByMember(member);
        // post 삭제: 해당 member가 작성한 post의 해시태그, 좋아요 기록 삭제
        postRepository.deleteAllByMember(member);

        // board
        // commentChildLike minus 처리: 해당 member가 다른 commentChild에 like한 경우
        List<CommentChildLike> commentChildLikeList = commentChildLikeRepository.findAllByMember(member);
        for (CommentChildLike commentChildLike : commentChildLikeList) {
            commentChildLike.getCommentChild().updateLikeNum(-1);
        }
        // commentChildLike 삭제: 해당 member가 commentChildLike한 기록 삭제
        commentChildLikeRepository.deleteAllByMember(member);
        // commentParentLike minus 처리: 해당 member가 다른 commentParent에 like한 경우
        List<CommentParentLike> commentParentLikeList = commentParentLikeRepository.findAllByMember(member);
        for (CommentParentLike commentParentLike : commentParentLikeList) {
            commentParentLike.getCommentParent().updateLikeNum(-1);
        }
        // commentParentLike 삭제: 해당 member가 commentParentLike한 기록 삭제
        commentParentLikeRepository.deleteAllByMember(member);
        // commentParent의 commentChildNum minus 처리: 해당 member가 작성한 commentChild가 달린 commentParent에 대한 처리
        List<CommentChild> commentChildList = commentChildRepository.findAllByMember(member);
        for (CommentChild commentChild : commentChildList) {
            commentChild.getCommentParent().updateCommentChildNum(-1);
        }
        // commentChild 삭제
        commentChildRepository.deleteAllByMember(member);
        // commentParent가 달린 board의 댓글 수 minus 처리
        List<CommentParent> commentParentList = commentParentRepository.findAllByMember(member);
        for (CommentParent commentParent : commentParentList) {
            commentParent.getBoard().updateCommentNum(-1);
        }
        // commentParent 삭제
        commentParentRepository.deleteAllByMember(member);
        // boardLike minus 처리
        List<BoardLike> boardLikeList = boardLikeRepository.findByMember(member);
        for (BoardLike boardLike : boardLikeList) {
            boardLike.getBoard().updateLikeNum(-1);
        }
        // boardLike 삭제
        boardLikeRepository.deleteAllByMember(member);
        // board 삭제: Controller에서 진행 (이중 주입 방지)

        // member
        memberPromotionRepository.deleteByMember(member);
        memberGradeUpRepository.deleteByMember(member);
        refreshTokenRepository.deleteByEmail(member.getEmail());
        memberRepository.delete(member);

        return new ResponseDto(true, "회원탈퇴가 완료되었습니다. 그동안 이용해 주셔서 감사합니다.", 200);
    }
}
