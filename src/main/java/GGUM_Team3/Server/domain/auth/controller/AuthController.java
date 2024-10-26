package GGUM_Team3.Server.domain.auth.controller;


import GGUM_Team3.Server.domain.auth.dto.request.MailSendRequest;
import GGUM_Team3.Server.domain.auth.dto.request.MailVerificationRequest;
import GGUM_Team3.Server.domain.auth.dto.request.GoogleLoginRequest;
import GGUM_Team3.Server.domain.auth.dto.request.LoginRequest;
import GGUM_Team3.Server.domain.auth.dto.response.LoginResponse;
import GGUM_Team3.Server.domain.auth.dto.request.SignupRequest;
import GGUM_Team3.Server.domain.auth.service.AuthService;
import GGUM_Team3.Server.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "회원 인증 관련 API")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    @Operation(summary = "일반 회원가입")
    @PostMapping(value = "/signup", consumes = "multipart/form-data")
    public ResponseEntity<Void>registerUser(@Validated @ModelAttribute SignupRequest signupRequest){
        userService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "구글로 첫 로그인시 기본 프로필 정보 등록", description = "구글로 처음 로그인했을 때 응답에서 isProfileComplete가 false이면 해당 API요청.")
    @PostMapping(path = "/signup/google", consumes = "multipart/form-data")
    public ResponseEntity<String> registerUserWithGoogle(@AuthenticationPrincipal String userId,
                                                         @ModelAttribute SignupRequest signupRequest){
        userService.signupWithGoogle(userId, signupRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "일반 로그인")
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse>login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @Operation(summary = "구글 로그인", description = "구글로 처음 로그인했을 때 응답에서 isProfileComplete가 false이면 /signup/google API요청.")
    @PostMapping("/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) {
        return ResponseEntity.ok(userService.loginWithGoogle(googleLoginRequest.getToken()));

    }
    @Operation(summary = "학교 이메일 인증 요청")
    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@RequestBody @Valid MailSendRequest mailSendRequest) {
        authService.sendVerificationCode(mailSendRequest.getEmail());
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "인증 코드 확인")
    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@RequestBody @Valid MailVerificationRequest mailVerificationRequest) {
        authService.verifyCode(mailVerificationRequest.getEmail(), mailVerificationRequest.getCode());
        return ResponseEntity.ok().build();
    }
}

