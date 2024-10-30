package GGUM_Team3.Server.domain.user.entity;

import GGUM_Team3.Server.domain.auth.dto.request.SignupRequest;
import GGUM_Team3.Server.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;

import static GGUM_Team3.Server.domain.user.service.UserService.getBirthDateAsLocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy="uuid")
    private String id;

    @Column
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String nickname;

    @Column
    private String password;

    @Column
    private String profileImageUrl;

    @Column(length = 16)
    private String profileMessage;

    @Column
    private String major;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Column
    Boolean isProfileComplete;

    @Column
    private LocalDate birthDate;

    @Builder
    public UserEntity(String id, String username, String email, String nickname, String password, String profileImageUrl, String profileMessage, String major, Gender gender, Boolean isProfileComplete, LocalDate birthDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.profileMessage = profileMessage;
        this.major = major;
        this.gender = gender;
        this.isProfileComplete = isProfileComplete;
        this.birthDate = birthDate;
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        if (this.birthDate != null && !this.birthDate.isAfter(currentDate)) {
            return Period.between(this.birthDate, LocalDate.now()).getYears();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid birth date");
        }
    }

    public void updateUser(SignupRequest signupRequest, String profileImageUrl, Boolean isProfileComplete) {
        if (signupRequest.getEmail() != null && !signupRequest.getEmail().isBlank()) {
            this.email = signupRequest.getEmail();
        }
        if (signupRequest.getNickname() != null && !signupRequest.getNickname().isBlank()) {
            this.nickname = signupRequest.getNickname();
        }
        if (profileImageUrl != null && !profileImageUrl.isBlank()) {
            this.profileImageUrl = profileImageUrl;
        }
        if (signupRequest.getProfileMessage() != null && !signupRequest.getProfileMessage().isBlank()) {
            this.profileMessage = signupRequest.getProfileMessage();
        }
        if (signupRequest.getMajor() != null && !signupRequest.getMajor().isBlank()) {
            this.major = signupRequest.getMajor();
        }
        if (signupRequest.getGender() != null) {
            this.gender = signupRequest.getGender();
        }
        if (isProfileComplete != null) {
            this.isProfileComplete = isProfileComplete;
        }
        if (signupRequest.getBirthDate() != null) {
            this.birthDate = getBirthDateAsLocalDate(signupRequest.getBirthDate());
        }
    }

    public void updateUser(String nickname, String profileMessage, String profileImageUrl) {
        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
        if (profileMessage != null && !profileMessage.isBlank()) {
            this.profileMessage = profileMessage;
        }
        if (profileImageUrl != null && !profileImageUrl.isBlank()) {
            this.profileImageUrl = profileImageUrl;
        }
    }
}