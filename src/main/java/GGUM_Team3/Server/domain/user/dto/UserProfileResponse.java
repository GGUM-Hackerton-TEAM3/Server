package GGUM_Team3.Server.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Schema(description = "유저 프로필 응답")
public class UserProfileResponse {
    private String profileImageUrl;
    private int age;
    private String major;
    private String nickname;
    private String profileMessage;

    public static UserProfileResponse of(String profileImageUrl,
                                         int age,
                                         String major,
                                         String nickname,
                                         String profileMessage) {
        return new UserProfileResponse(
                profileImageUrl,
                age,
                major,
                nickname,
                profileMessage);
    }
}
