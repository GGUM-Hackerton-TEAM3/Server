package GGUM_Team3.Server.domain.user.controller;


import GGUM_Team3.Server.domain.image.service.ImageService;
import GGUM_Team3.Server.domain.user.dto.ResponseDTO;
import GGUM_Team3.Server.domain.user.dto.UserDTO;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.service.UserService;
import GGUM_Team3.Server.sercurity.GoogleTokenVerifier;
import GGUM_Team3.Server.sercurity.TokenProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    @PostMapping("/signup")
    public ResponseEntity<?>registerUser(@ModelAttribute UserDTO userDTO){ //todo : 임시입니다.
        try {
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .profileImageUrl(imageService.saveImage(userDTO.getProfileImage()))
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();

            userService.create(user);

            return ResponseEntity.ok().build();
        } catch(Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?>authenticate(@RequestBody UserDTO userDTO){
        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

        if(user !=null){
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleAuthenticate(@RequestBody Map<String, String> body) {
        String token = body.get("token");

        String userId = googleTokenVerifier.verify(token); // 구글 토큰 검증
        String email = googleTokenVerifier.getEmail(token); // 구글 토큰에서 이메일 추출

        if (userId != null && email != null) {
            // 이메일로 기존 사용자 확인
            UserEntity user = userService.getByEmail(email);

            if (user == null) {
                // 새로운 Google 사용자일 경우 UserEntity 생성 및 저장
                user = UserEntity.builder()
                        .email(email)
                        .username(googleTokenVerifier.getName(token)) // 구글에서 이름 가져오기
                        .build();
                userService.create(user);
            }

            // JWT 생성 및 반환
            final String jwtToken = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .token(jwtToken)
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Google token validation failed")
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

}
