package com.project.triport.service;

import com.project.triport.entity.Member;
import com.project.triport.repository.MemberRepository;
import com.project.triport.requestDto.MemberMailRequestDto;
import com.project.triport.responseDto.ResponseDto;
import com.project.triport.util.MailHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Random;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class MemberMailService {

    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private @Value("${spring.mail.username}") String fromMail;

    // 임시 비밀번호 안내 메일 발송
    @Transactional
    public ResponseDto sendTempPwd(MemberMailRequestDto memberMailRequestDto) {
        Member member = memberRepository.findByEmail(memberMailRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("입력하신 이메일로 가입된 사용자가 없습니다."));

        String tmpPwd = generateTempPwd();
        member.updateTmpPassword(tmpPwd, passwordEncoder);

        try {
            MailHandler mailHandler = tempPwdMail(member, tmpPwd);
            mailHandler.send();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseDto(true, "회원님의 이메일로 임시 비밀번호를 발송하였습니다.");
    }

    // 임시 비밀번호 안내 메일 내용
    public MailHandler tempPwdMail(Member member, String tmpPwd) throws MessagingException, IOException {
        MailHandler mailHandler = new MailHandler(mailSender);

        // 받는 사람
        mailHandler.setTo(member.getEmail());
        // 보내는 사람
        mailHandler.setFrom(fromMail);
        // 제목
        mailHandler.setSubject("[Triport] 회원님의 임시 비밀번호를 확인해 주세요.");
        // 내용 (HTML Layout)
        String htmlContent = "<img src='cid:tripper_with_logo' style='width:300px'> <br> <br>" +
                "<p> 안녕하세요, 여행의 설레임 Triport✈️ 입니다! <br>" +
                "아래의 임시 비밀번호로 로그인하여 주시기 바랍니다. <br> <br>" +
                "임시 비밀번호: " + tmpPwd + "<br> <br>" +
                "로그인 후, 반드시 비밀번호를 변경하여 주세요.😊 <br>" +
                "이용해주셔서 감사합니다~!♥️ <p> <br> <br>";
        mailHandler.setText(htmlContent, true);
        // 이미지 삽입
        mailHandler.setInline("tripper_with_logo", "static/tripper_with_logo.png");

        return mailHandler;
    }

    // 임시 비밀번호 생성: 랜덤 영문자+숫자
    public String generateTempPwd() {

        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 12;

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
