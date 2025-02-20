package com.danjitalk.danjitalk.api.mail;

import com.danjitalk.danjitalk.application.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class MailController {

    private final MailService mailService;

    // 인증 이메일 전송
    @PostMapping("/certification-code/send")
    public ResponseEntity<Void> sendCertificationCode(@RequestParam String mail) {
        mailService.sendVerificationEmail(mail);
        return ResponseEntity.ok().build();
    }
}
