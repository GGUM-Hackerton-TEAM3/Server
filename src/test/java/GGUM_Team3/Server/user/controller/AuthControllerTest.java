package GGUM_Team3.Server.user.controller;

import GGUM_Team3.Server.domain.auth.dto.request.SignupRequest;
import GGUM_Team3.Server.domain.auth.service.AuthService;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.service.UserService;
import GGUM_Team3.Server.global.sercurity.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private AuthService authService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupRequest signupRequest;


    @Test
    public void contextLoads() {
        assertThat(true).isTrue();
    }

    @BeforeEach
    public void setUp() {
        SignupRequest signupRequest= SignupRequest.builder()
                .email("testuser@example.com")
                .username("testuser")
                .password("password123")
                .build();

        // PasswordEncoder의 동작을 Mocking
        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenAnswer(invocation -> "encoded_" + invocation.getArgument(0));
    }

    @Test
    public void testSignup() throws Exception {
        // Mock 회원가입 성공 시나리오 설정
        UserEntity savedUser = UserEntity.builder()
                .email(signupRequest.getEmail())
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();

        Mockito.when(userService.create(Mockito.any(UserEntity.class))).thenReturn(savedUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(signupRequest.getEmail()))
                .andExpect(jsonPath("$.username").value(signupRequest.getUsername()));
    }

    @Test
    public void testSignin() throws Exception {
        // Mock 로그인 성공 시나리오 설정
        UserEntity existingUser = UserEntity.builder()
                .email(signupRequest.getEmail())
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();

        Mockito.when(authService.getByCredentials(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(existingUser);

        String jwtToken = "mock-jwt-token";
        Mockito.when(tokenProvider.create(Mockito.any(UserEntity.class))).thenReturn(jwtToken);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken))
                .andExpect(jsonPath("$.email").value(signupRequest.getEmail()));
    }

    @Test
    public void testSigninWithInvalidCredentials() throws Exception {
        // Mock 로그인 실패 시나리오 설정
        Mockito.when(authService.getByCredentials(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Login failed"));
    }
}
