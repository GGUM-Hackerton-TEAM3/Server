package GGUM_Team3.Server.domain.user.entity;

import GGUM_Team3.Server.domain.user.dto.SignupRequest;
import GGUM_Team3.Server.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.Period;

import static GGUM_Team3.Server.domain.user.service.UserService.getBirthDateAsLocalDate;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
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

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        if (this.birthDate != null && !this.birthDate.isAfter(currentDate)) {
            return Period.between(this.birthDate, LocalDate.now()).getYears();
        } else {
            throw new IllegalArgumentException("Invalid birth date");
        }
    }

    public void updateProfile(SignupRequest signupRequest, String profileImageUrl, Boolean isProfileComplete) {
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

}