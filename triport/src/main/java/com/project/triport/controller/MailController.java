package com.project.triport.controller;

import com.project.triport.requestDto.MailRequestDto;
import com.project.triport.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//@Controller
//@AllArgsConstructor
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/reset/password")
    public String sendTempPwd(@RequestBody MailRequestDto mailRequestDto) {
        return mailService.sendTempPwd(mailRequestDto);
    }

//    @PutMapping("/reset/password")
//    public void
//
//    @GetMapping()
//    public String dispMail() {
//        return "mail";
//    }

}
