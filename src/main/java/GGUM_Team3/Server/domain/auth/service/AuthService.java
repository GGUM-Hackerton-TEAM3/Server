package GGUM_Team3.Server.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public void sendVerificationCode(String email) {
        if (isCatholicEmail(email)) {
            String code = generateVerificationCode();

            redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Oneul Verification Code");
            message.setText("Your verification code is: " + code);
            mailSender.send(message);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가톨릭대 이메일이 아닙니다.");
        }
    }

    public void verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(email);
        if(!code.equals(storedCode)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않거나 만료되었습니다.");
        redisTemplate.delete(email);
    }

    public boolean isCatholicEmail(String email) {
        String[] strings = email.split("@");
        if (strings.length == 2) {
            String domain = strings[1];
            return domain.equals("catholic.ac.kr");
        }
        return false;
    }
}
