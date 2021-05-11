package com.project.triport.controller;

import com.project.triport.requestDto.MailRequestDto;
import com.project.triport.responseDto.MailResponseDto;
import com.project.triport.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@Controller
//@AllArgsConstructor
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/reset/password")
    public ResponseEntity<MailResponseDto> sendTempPwd(@RequestBody MailRequestDto mailRequestDto) {
        return ResponseEntity.ok(mailService.sendTempPwd(mailRequestDto));
    }

//    @PutMapping("/reset/password")
//    public void
//
//    @GetMapping()
//    public String dispMail() {
//        return "mail";
//    }

}
