package GGUM_Team3.Server.domain.user.dto;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class UserProfileUpdateRequest {
    private String nickname;
    private String profileMessage;
    private MultipartFile profileImage;
}