// 모임 관련 엔티티
package GGUM_Team3.Server.meeting.entity;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private UUID creatorId;

    @Column(nullable = false)
    private String title;
    private String description;
    private int maxParticipants;
    private LocalDateTime startTime;
    private String region; // 지역 필드
    private String notice; // 공지 필드
    private UUID chatRoomId; // 채팅방ID 필드
    private UUID categoryId; // 카테고리ID 필드

    @ManyToMany
    @JoinTable(
            name = "meeting_members",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> participants;
}
