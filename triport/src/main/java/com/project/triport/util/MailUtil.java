package com.project.triport.util;

import com.project.triport.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender mailSender;
    private @Value("${spring.mail.username}") String fromMail;


    // *임시 비밀번호 Mail 발송
    @Async
    public void TempPwdMail(Member member, String tmpPwd) throws IOException, MessagingException {

        MailHandler mailHandler = new MailHandler(mailSender);
        String nickname = member.getNickname();

        // 임시 비밀번호 Mail 내용
        // 받는 사람
        mailHandler.setTo(member.getEmail());
        // 보내는 사람
        mailHandler.setFrom(fromMail);
        // 제목
        mailHandler.setSubject("[TRIPORT] "+nickname+"님의 임시 비밀번호를 확인해 주세요.");
        // 내용 (HTML Layout)
        String htmlContent = "<img src='cid:tripper_with_logo' style='width:300px'> <br> <br>" +
                "<p style='font-size: medium; color: black'>" +
                "안녕하세요, 여행의 설레임 <span style='color: #1f70de; font-weight: bold'>TRIPORT✈️</span>️ 입니다! <br>" +
                "아래의 임시 비밀번호로 로그인하여 주시기 바랍니다. <br> <br>" +
                "임시 비밀번호: " + tmpPwd + "<br> <br>" +
                "<span style='color: #FD574A; font-weight: bold'>로그인 후, 반드시 비밀번호를 변경하여 주세요.😊</span> <br>" +
                "이용해주셔서 감사합니다~!💙️️<br><br>" +
                "<a href='https://triport.kr' style='font-weight: bold'>TRIPORT✈️  바로가기!</a>" +
                "<p> <br> <br>";
        mailHandler.setText(htmlContent, true);
        // 이미지 삽입
        mailHandler.setInline("tripper_with_logo", "static/tripper_with_logo.png");

        mailHandler.send();
    }

    // *Trils LikeNum 5개인 member에게 promotion 메일 발송
    @Async
    public void trilsPromoMail(Member member) throws MessagingException, IOException {
        MailHandler mailHandler = new MailHandler(mailSender);
        String nickname = member.getNickname();

        // 받는 사람
        mailHandler.setTo(member.getEmail());
        // 보내는 사람
        mailHandler.setFrom(fromMail);
        // 제목
        mailHandler.setSubject("[TRIPORT] 회원님의 Trils 영상이 HOT 해요! 😍");
        // 내용 (HTML Layout)
        String htmlContent ="<p>" +
                "<img src='cid:trils_promo' style='height:480px' align='left' hspace='15'>" +
                "<span style='font-size: large'; color: black'>" +
                "<img src='cid:triport_logo' style='width:200px'> <br><br><br>" +
                "안녕하세요, 여행의 설레임 <span style='color: #1f70de; font-weight: bold'>TRIPORT✈️</span> 입니다! <br>" +
                nickname+"님의 <span style='color: #1f70de; font-weight: bold'>Trils</span> <span style='font-weight: bold'>영상 좋아요가 벌써 5개에요!😆🎉</span><br><br><br>" +
                "다른 <span style='font-weight: bold'>TRAVELER</span>분들이 영상 속 이야기를 궁금해 하시는 것 같아요~<br>" +
                "<span style='font-weight: bold'>특별했던 "+nickname+"님의 여행을 <span style='color: #1f70de; font-weight: bold'>Trilog</span>에 기록해 주세요!</span><br>" +
                "멋진 추억을 다른 분들과 공유하면서 좋은 에너지를 나누어주시기 바랍니다!🥰 <br><br><br>" +
                "<span style='font-weight: bold'>(속닥속닥) <span style='color: #1f70de; font-weight: bold'>Trilog</span>에 여행 이야기를 기록하면, " +
                "<span style='color: #FD574A; font-weight: bold'>TRAVEL Editor로 Level Up!</span> 해드려요..!🤫</span> <br><br><br>" +
                "<a href='https://triport.kr' style='font-weight: bold'>💙️"+nickname+"님의 Trils 반응 보고, Trilog 작성하러 가기!💙</a>" +
                "<br> <br>" +
                "</span>" +
                "<p>";
        mailHandler.setText(htmlContent, true);
        // 이미지 삽입
        mailHandler.setInline("triport_logo", "static/triport_logo.png");
        mailHandler.setInline("trils_promo", "static/trils_promo.png");

        mailHandler.send();
    }
}
