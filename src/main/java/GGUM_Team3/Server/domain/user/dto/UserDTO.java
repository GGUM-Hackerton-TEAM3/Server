package GGUM_Team3.Server.domain.user.dto;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;
    private String email;
    private String username;
    private String password;
    private MultipartFile profileImage;
    private String id;

    public static UserDTO fromEntity(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId()) // UserEntity의 id
                .email(userEntity.getEmail()) // UserEntity의 email
                .username(userEntity.getUsername()) // UserEntity의 username
                .password(userEntity.getPassword()) // 보통 password는 DTO에 포함하지 않는 것이 좋지만, 필요시 포함
                .build();
    }
}