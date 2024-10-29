package GGUM_Team3.Server.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UserProfileResponse {
    private final String profileImageUrl;
    private final Integer age;
    private final String major;
    private final String nickname;
    private final String profileMessage;

    public static UserProfileResponse of(final String profileImageUrl,
                                         final Integer age,
                                         final String major,
                                         final String nickname,
                                         final String profileMessage) {
        return new UserProfileResponse(
                profileImageUrl,
                age,
                major,
                nickname,
                profileMessage);
    }
}
