package GGUM_Team3.Server.domain.auth.dto.request;

import GGUM_Team3.Server.domain.user.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private MultipartFile profileImage;
    private String nickname;
    @Schema(description = "성별. MALE: 남자, FEMALE: 여자")
    private Gender gender;
    private String profileMessage;
    private String major;
    private String birthDate;
}
