package GGUM_Team3.Server.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private String meetingId;  // 좋아요할 모임 ID
    private String userId;     // 좋아요를 누른 사용자 ID
}
