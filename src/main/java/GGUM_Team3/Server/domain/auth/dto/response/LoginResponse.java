package GGUM_Team3.Server.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Schema(name = "로그인 응답 DTO")
public class LoginResponse {
    @Schema(description = "사용자의 이메일", example = "user@example.com")
    private String accessToken;

    @Schema(description = "프로필", example = "true")
    private Boolean isProfileComplete;

    public static LoginResponse of(String token, Boolean isProfileComplete) {
        return new LoginResponse(token, isProfileComplete);
    }
}
