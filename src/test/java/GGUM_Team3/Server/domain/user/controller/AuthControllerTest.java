package GGUM_Team3.Server.domain.user.controller;

import GGUM_Team3.Server.domain.auth.dto.request.LoginRequest;
import GGUM_Team3.Server.domain.auth.dto.request.SignupRequest;
import GGUM_Team3.Server.domain.user.entity.Gender;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private SignupRequest signupRequest;


    @BeforeEach
    public void setUp() {
        MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        signupRequest = SignupRequest.builder()
                .email("testuser@example.com")
                .username("testuser")
                .password("password123")
                .birthDate("20000101")
                .profileImage(profileImage)
                .nickname("Test Nickname")
                .gender(Gender.MALE)
                .profileMessage("message")
                .major("Computer Science")
                .build();
    }

    @Test
    @DisplayName("회원가입")
    @Transactional
    public void signupTest() throws Exception {
        // when & then
        mockMvc.perform(multipart("/api/auth/signup")
                        .file((MockMultipartFile) signupRequest.getProfileImage())
                        .param("username", signupRequest.getUsername())
                        .param("password", signupRequest.getPassword())
                        .param("email", signupRequest.getEmail())
                        .param("birthDate", signupRequest.getBirthDate())
                        .param("nickname", signupRequest.getNickname())
                        .param("gender", signupRequest.getGender().name())
                        .param("profileMessage", signupRequest.getProfileMessage())
                        .param("major", signupRequest.getMajor())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("일반 로그인 성공")
    @Transactional
    public void signinSuccessTest() throws Exception {
        // given
        UserEntity existingUser = UserEntity.builder()
                .email(signupRequest.getEmail())
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))  // 인코딩된 비밀번호로 저장
                .build();

        userRepository.save(existingUser);

        LoginRequest loginRequest = new LoginRequest(
                signupRequest.getEmail(),
                signupRequest.getPassword()
        );

        // when & then
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @DisplayName("일반 로그인 실패")
    @Transactional
    public void signinFailedTest() throws Exception {
        // given
        UserEntity existingUser = UserEntity.builder()
                .email(signupRequest.getEmail())
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))  // 인코딩된 비밀번호로 저장
                .build();

        userRepository.save(existingUser);

        LoginRequest loginRequest = new LoginRequest(
                signupRequest.getEmail(),
                "wrong_password"  // 올바르지 않은 비밀번호
        );

        // when & then
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())  // 상태 코드 400 기대
                .andExpect(jsonPath("$.message").value("Login failed"));  // 오류 메시지 검증
    }
}
