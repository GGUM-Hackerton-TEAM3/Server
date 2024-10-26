package GGUM_Team3.Server.domain.user.service;

import GGUM_Team3.Server.domain.image.service.ImageService;
import GGUM_Team3.Server.domain.user.dto.UserProfileResponse;
import GGUM_Team3.Server.domain.user.dto.UserProfileUpdateRequest;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import GGUM_Team3.Server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;

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

    public Optional<UserEntity> getById(String userId) {
        return userRepository.findById(userId);
    }

    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
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

    public UserProfileResponse getUserProfile(String userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found"));

        return UserProfileResponse.of(
                user.getProfileImageUrl(),
                user.getAge(),
                user.getMajor(),
                user.getNickname(),
                user.getProfileMessage()
        );
    }

    @Transactional
    public UserProfileResponse updateUserProfile(String userId, UserProfileUpdateRequest updateRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.updateUser(
                updateRequest.getNickname(),
                updateRequest.getProfileMessage(),
                imageService.saveImage(updateRequest.getProfileImage())
        );
        return new UserProfileResponse(
                user.getProfileImageUrl(),
                user.getAge(),
                user.getMajor(),
                user.getNickname(),
                user.getProfileMessage()
        );
    }

}
