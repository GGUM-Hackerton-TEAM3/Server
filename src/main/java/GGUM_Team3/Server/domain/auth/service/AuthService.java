package GGUM_Team3.Server.domain.auth.service;

import GGUM_Team3.Server.domain.auth.dto.request.LoginRequest;
import GGUM_Team3.Server.domain.auth.dto.request.SignupRequest;
import GGUM_Team3.Server.domain.auth.dto.response.LoginResponse;
import GGUM_Team3.Server.domain.image.service.ImageService;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.repository.UserRepository;
import GGUM_Team3.Server.domain.user.service.UserService;
import GGUM_Team3.Server.global.sercurity.GoogleTokenVerifier;
import GGUM_Team3.Server.global.sercurity.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static GGUM_Team3.Server.domain.user.service.UserService.getBirthDateAsLocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;

    public LoginResponse login(final LoginRequest loginRequest){
        final UserEntity user = getByCredentials(loginRequest.getEmail(), loginRequest.getPassword(), passwordEncoder);
        if(user !=null){
            final String token = tokenProvider.create(user);
            return LoginResponse.of(token, user.getIsProfileComplete());

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login failed");
        }
    }

    public LoginResponse loginWithGoogle(final String googleToken) {
        String userId = googleTokenVerifier.verify(googleToken); // 구글 토큰 검증
        String email = googleTokenVerifier.getEmail(googleToken); // 구글 토큰에서 이메일 추출

        if (userId != null && email != null) {
            // 이메일로 기존 사용자 확인
            UserEntity user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                // 새로운 Google 사용자일 경우 UserEntity 생성 및 저장
                user = UserEntity.builder()
                        .email(email)
                        .username(googleTokenVerifier.getName(googleToken)) // 구글에서 이름 가져오기
                        .password(passwordEncoder.encode(UUID.randomUUID().toString())) // 보안상 임시 비밀번호 설정
                        .isProfileComplete(false) // 처음 로그인 시 회원정보 상세 기입 여부 표시를 위해 false로 초기화
                        .build();
                userService.create(user);
            }

            // JWT 생성 및 반환
            final String jwtToken = tokenProvider.create(user);
            return LoginResponse.of(jwtToken, user.getIsProfileComplete());
        } else {
            throw new RuntimeException("Google token validation failed");
        }
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userService.getByEmail(email);
        if(encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }

    @Transactional
    public void signup(final SignupRequest signupRequest) {
        userService.validateAlreadyExistEmail(signupRequest.getEmail());

        UserEntity user = UserEntity.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .nickname(signupRequest.getNickname())
                .profileImageUrl(imageService.saveImage(signupRequest.getProfileImage()))
                .profileMessage(signupRequest.getProfileMessage())
                .major(signupRequest.getMajor())
                .birthDate(getBirthDateAsLocalDate(signupRequest.getBirthDate()))
                .gender(signupRequest.getGender())
                .isProfileComplete(true)
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void signupWithGoogle(final String userId, final SignupRequest signupRequest) {
        UserEntity user = userService.getById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.updateUser(
                signupRequest,
                signupRequest.getProfileImage() != null ? imageService.saveImage(signupRequest.getProfileImage()) : null,
                true);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    public void sendVerificationCode(String email) {
        if (isCatholicUniversityEmail(email)) {
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

    public boolean isCatholicUniversityEmail(String email) {
        String[] strings = email.split("@");
        if (strings.length == 2) {
            String domain = strings[1];
            return domain.equals("catholic.ac.kr");
        }
        return false;
    }
}
