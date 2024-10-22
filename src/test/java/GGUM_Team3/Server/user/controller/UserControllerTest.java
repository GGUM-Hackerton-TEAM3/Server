package GGUM_Team3.Server.user.controller;

import GGUM_Team3.Server.sercurity.TokenProvider;
import GGUM_Team3.Server.user.dto.UserDTO;
import GGUM_Team3.Server.user.entity.UserEntity;
import GGUM_Team3.Server.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;


    @Test
    public void contextLoads() {
        assertThat(true).isTrue();
    }

    @BeforeEach
    public void setUp() {
        userDTO = UserDTO.builder()
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
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        Mockito.when(userService.create(Mockito.any(UserEntity.class))).thenReturn(savedUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()))
                .andExpect(jsonPath("$.username").value(userDTO.getUsername()));
    }

    @Test
    public void testSignin() throws Exception {
        // Mock 로그인 성공 시나리오 설정
        UserEntity existingUser = UserEntity.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        Mockito.when(userService.getByCredentials(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(existingUser);

        String jwtToken = "mock-jwt-token";
        Mockito.when(tokenProvider.create(Mockito.any(UserEntity.class))).thenReturn(jwtToken);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    public void testSigninWithInvalidCredentials() throws Exception {
        // Mock 로그인 실패 시나리오 설정
        Mockito.when(userService.getByCredentials(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Login failed"));
    }
}
