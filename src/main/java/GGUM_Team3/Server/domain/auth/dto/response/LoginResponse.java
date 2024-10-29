package GGUM_Team3.Server.domain.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponse {
    private final String accessToken;

    @Schema(description = "기본 프로필 정보 등록 여부", example = "true")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Boolean isProfileComplete;

    public static LoginResponse of(final String token, final Boolean isProfileComplete) {
        return new LoginResponse(token, isProfileComplete);
    }
}
