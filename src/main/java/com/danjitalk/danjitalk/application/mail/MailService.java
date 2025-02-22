package com.danjitalk.danjitalk.application.mail;

import static com.danjitalk.danjitalk.common.util.RandomCodeGeneratorUtil.generateRandomNumber;

import com.danjitalk.danjitalk.common.exception.BadRequestException;
import com.danjitalk.danjitalk.common.exception.ConflictException;
import com.danjitalk.danjitalk.infrastructure.repository.mail.RedisRepository;
import com.danjitalk.danjitalk.infrastructure.repository.user.member.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private static final int NUMBER_CODE_LENGTH = 6;

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final RedisRepository redisRepository;
    private final MemberRepository memberRepository;

    // 1. 인증번호 받기 버튼을 누른다.
    // 2. 인증번호를 생성하고 redis에 저장한다.
    // 3. redis에 저장된 번호를 보낸다.
    public void sendVerificationEmail(String mail) {

        // 메일중복 시 메소드 예외
        if (memberRepository.existsByEmail(mail)) {
            throw new ConflictException("이미 가입된 메일입니다. : " + mail);
        }

        // 랜덤번호 생성
        int numberCode = generateRandomNumber(NUMBER_CODE_LENGTH);
        // 레디스에 저장
        redisRepository.setValues(mail, String.valueOf(numberCode), Duration.ofMinutes(3));

        MimeMessage message = mailSender.createMimeMessage();
        Map<String, String> map = new HashMap<>();
        map.put("code", String.valueOf(numberCode));

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, false, "UTF-8");
            mimeMessageHelper.setTo(mail); // 메일 수신자
            mimeMessageHelper.setSubject("이메일 인증"); // 메일 제목
            mimeMessageHelper.setText(setContext(map), true); // 메일 본문 내용, HTML 여부
            mailSender.send(message);

            log.info("Successfully created mail");
        } catch (MessagingException e) {
            log.error("Failed to create mail: {}", e.getMessage());
        } catch (MailException e) {
            log.error("Failed to send mail: {}", e.getMessage());
        }
    }

    private String setContext(Map<String, String> map) {
        Context context = new Context();
        map.forEach(context::setVariable);
        return templateEngine.process("mail", context);
    }

    public void validateEmailAuthCode(String mail, String code) {
        String issuedCode = redisRepository.getValues(mail);
        if (issuedCode == null) {
            throw new BadRequestException("해당 이메일에 대해 발급된 인증 코드가 없거나 만료되었습니다.");
        }

        boolean isMatch = Objects.equals(issuedCode, code);
        if(!isMatch) {
            throw new BadRequestException("인증에 실패하였습니다. 올바른 인증 코드를 입력하세요.");
        }
    }
}
