package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.entity.MemberPromotion;
import com.project.triport.entity.Post;
import com.project.triport.jwt.CustomUserDetails;
import com.project.triport.repository.MemberPromotionRepository;
import com.project.triport.repository.MemberRepository;
import com.project.triport.repository.PostRepository;
import com.project.triport.requestDto.MemberMailRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class MemberMailService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final MemberPromotionRepository memberPromotionRepository;
    private final MailUtil mailUtil;

    // 임시 비밀번호 안내 메일 발송
    @Transactional
    public ResponseDto sendTempPwd(MemberMailRequestDto memberMailRequestDto) {
        boolean existsByEmail = memberRepository.existsByEmail(memberMailRequestDto.getEmail());
        if (!existsByEmail) {
            return new ResponseDto(false, "가입되지 않은 이메일 입니다.", 400);
        }

        Member member = getAuthMember();
        
        if (!(member.getKakaoId() == null)) {
            return new ResponseDto(false, "카카오 로그인 사용자는 비밀번호 찾기 이용이 불가합니다.🥲", 400);
        }

        String tmpPwd = mailUtil.TempPwdMail(member);
        member.updatePassword(tmpPwd);

        return new ResponseDto(true, "회원님의 이메일로 임시 비밀번호를 발송하였습니다.", 200);
    }

    // Trils 좋아요 5개 + TRAVELER 인 member에게
    // Trilog 작성 promotion 메일 발송
    // 여기서 Member는 '좋아요를 받은 작성자=author'인 점 주의
    @Transactional
    public void sendPromotion(Long postId) throws IOException, MessagingException {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException("해당하는 post가 존재하지 않습니다.")
        );

        Long likeNum = post.getLikeNum();
        Member author = post.getMember();
        MemberPromotion memberPromotion = memberPromotionRepository.findByMember(author);
        boolean isEnabled = memberPromotion.isTrilsFiveLikePromo();

        mailUtil.trilsPromoMail(likeNum, author, isEnabled);
        memberPromotion.updateTrilsPromo(author, true);
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
