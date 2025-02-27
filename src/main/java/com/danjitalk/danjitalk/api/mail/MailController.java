package com.danjitalk.danjitalk.api.mail;

import com.danjitalk.danjitalk.application.mail.MailService;
import com.danjitalk.danjitalk.domain.mail.dto.MailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<Void> sendCertificationCode(@RequestBody MailRequest request) {
        mailService.sendVerificationEmail(request);
        return ResponseEntity.ok().build();
    }

    // 인증번호 일치여부 확인
    @GetMapping("/certification-code/verify")
    public ResponseEntity<Boolean> checkCertificationCode(
        @RequestParam String email,
        @RequestParam("code") String validationCode
    ) {
        mailService.validateEmailAuthCode(email, validationCode);
        return ResponseEntity.ok().build();
    }
}
