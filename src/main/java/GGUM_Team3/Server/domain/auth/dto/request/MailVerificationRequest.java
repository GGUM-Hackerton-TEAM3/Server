package GGUM_Team3.Server.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MailVerificationRequest {
    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String code;
}
