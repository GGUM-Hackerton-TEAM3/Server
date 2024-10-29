package GGUM_Team3.Server.domain.user.service;

import GGUM_Team3.Server.domain.image.service.ImageService;
import GGUM_Team3.Server.domain.user.dto.UserProfileResponse;
import GGUM_Team3.Server.domain.user.dto.UserProfileUpdateRequest;
import GGUM_Team3.Server.domain.user.entity.Gender;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private UserService userService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = UserEntity.builder()
                .id("test-user-id")
                .email("test@catholic.ac.kr")
                .username("testuser")
                .password("password123")
                .birthDate(LocalDate.parse("2000-02-18"))
                .profileImageUrl("http://example.com/image.jpg")
                .nickname("Test Nickname")
                .gender(Gender.MALE)
                .profileMessage("message")
                .major("Computer Science")
                .build();

    }

    @Test
    @DisplayName("유저 프로필 조회")
    void getUserProfileTest() {
        // given
        given(userRepository.findById(mockUser.getId())).willReturn(Optional.of(mockUser));

        // when
        UserProfileResponse userProfileResponse = userService.getUserProfile(mockUser.getId());

        // then
        assertEquals(mockUser.getNickname(), userProfileResponse.getNickname());
        assertEquals(mockUser.getProfileMessage(), userProfileResponse.getProfileMessage());
        assertEquals(mockUser.getProfileImageUrl(), userProfileResponse.getProfileImageUrl());
        assertEquals(mockUser.getBirthDate().until(LocalDate.now()).getYears(), userProfileResponse.getAge());
        assertEquals(mockUser.getMajor(), userProfileResponse.getMajor());
    }

    @Test
    @DisplayName("유저 프로필 수정")
    void updateUserProfileTest() {
        // Given
        MockMultipartFile mockImage = new MockMultipartFile(
                "profileImage",
                "test-image.jpg",
                "image/jpeg",
                "test-image-content".getBytes()
        );
        UserProfileUpdateRequest updateRequest = UserProfileUpdateRequest.builder()
                .nickname("UpdatedUser")
                .profileMessage("Updated Message")
                .profileImage(mockImage)
                .build();
        String updatedImageUrl = "http://example.com/updated-image.jpg";

        given(userRepository.findById(mockUser.getId())).willReturn(Optional.of(mockUser));
        given(imageService.saveImage(any())).willReturn(updatedImageUrl);

        // when
        UserProfileResponse response = userService.updateUserProfile(mockUser.getId(), updateRequest);

        // then
        assertEquals(updateRequest.getNickname(), response.getNickname());
        assertEquals(updateRequest.getProfileMessage(), response.getProfileMessage());
        assertEquals(updatedImageUrl, response.getProfileImageUrl());

        // Verify interactions
        verify(userRepository, times(1)).findById(mockUser.getId());
        verify(imageService, times(1)).saveImage(any());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}
