package GGUM_Team3.Server.domain.user.service;

import GGUM_Team3.Server.domain.user.repository.UserRepository;

import GGUM_Team3.Server.domain.image.service.ImageService;
import GGUM_Team3.Server.domain.auth.dto.request.LoginRequest;
import GGUM_Team3.Server.domain.auth.dto.response.LoginResponse;
import GGUM_Team3.Server.domain.auth.dto.request.SignupRequest;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.global.sercurity.GoogleTokenVerifier;
import GGUM_Team3.Server.global.sercurity.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final GoogleTokenVerifier googleTokenVerifier;


    @Transactional
    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}",email);
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

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
                create(user);
            }

            // JWT 생성 및 반환
            final String jwtToken = tokenProvider.create(user);
            return LoginResponse.of(jwtToken, user.getIsProfileComplete());
        } else {
            throw new RuntimeException("Google token validation failed");
        }
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = getByEmail(email);
        if(encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }


    public Optional<UserEntity> getById(String userId) {
        return userRepository.findById(userId);
    }

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
    }

    @Transactional
    public void signup(final SignupRequest signupRequest) {
        validateAlreadyExistEmail(signupRequest.getEmail());

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
        UserEntity user = getById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.updateProfile(
                signupRequest,
                signupRequest.getProfileImage() != null ? imageService.saveImage(signupRequest.getProfileImage()) : null,
                true);
    }

    public void validateAlreadyExistEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
    }

    public static LocalDate getBirthDateAsLocalDate(String birthDateString) {
        if(birthDateString == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        try {
            return LocalDate.parse(birthDateString, formatter);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid birth date format");
        }
    }
}
