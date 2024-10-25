package GGUM_Team3.Server.meeting.entity;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String creatorId;

    @Column(nullable = false)
    private String title;
    private String description;
    private int maxParticipants;
    private LocalDateTime startTime;
    private String region; // 지역 필드
    private String notice; // 공지 필드
    private String chatRoomId; // 채팅방ID 필드
    private String categoryId; // 카테고리ID 필드

    @ElementCollection
    private List<String> tags;

    @ManyToMany
    @JoinTable(
            name = "meeting_members",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> participants = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "meeting_likes",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> likes = new HashSet<>();

    // 좋아요 개수 반환 메서드
    public int getLikesCount() {
        return likes.size();
    }
}