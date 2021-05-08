package com.project.triport.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MailHandler {
    private JavaMailSender sender;
    private MimeMessage mimeMessage;
    private MimeMessageHelper messageHelper;

    //생성자
    public MailHandler(JavaMailSender javaMailSender) throws MessagingException {
        this.sender = javaMailSender;
        mimeMessage = javaMailSender.createMimeMessage();
        messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    }

    //보내는 사람
    public void setFrom(String fromAddress) throws MessagingException {
        messageHelper.setFrom(fromAddress);
    }

    //받는 사람
    public void setTo(String email) throws MessagingException {
        messageHelper.setTo(email);
    }

    //제목
    public void setSubject(String subject) throws MessagingException {
        messageHelper.setSubject(subject);
    }

    //메일 내용
    public void setText(String text, boolean useHtml) throws MessagingException {
        messageHelper.setText(text, useHtml);
    }

//    //첨부 파일
//    public void setAttach(String displayFileName, String pathToAttachment) throws MessagingException, IOException {
//        File file = new ClassPathResource(pathToAttachment).getFile();
//        FileSystemResource fileSystemResource = new FileSystemResource(file);
//
//        messageHelper.addAttachment(displayFileName, fileSystemResource);
//    }

    //이미지 삽입
    public void setInline(String contentId, String pathToInline) throws MessagingException, IOException {
//        File file = new ClassPathResource(pathToInline).getFile();
//        FileSystemResource fileSystemResource = new FileSystemResource(file);

        ClassPathResource classPathResource = new ClassPathResource(pathToInline);
        InputStream inputStream = classPathResource.getInputStream();
        File file = File.createTempFile("file", ".png");

        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        messageHelper.addInline(contentId, file);
    }

    //발송
    public void send() {
        try {
            sender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
