// 모임 관련 엔티티
package GGUM_Team3.Server.domain.meeting.entity;

import GGUM_Team3.Server.global.BaseEntity;
import GGUM_Team3.Server.domain.tag.category.entity.CategoryEntity;
import GGUM_Team3.Server.domain.tag.hashtag.entity.MeetingHashtagEntity;
import GGUM_Team3.Server.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String creatorId;

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    private CategoryEntity category;

    @Column(nullable = false)
    private String title;
    private String description;
    private int maxParticipants;
    private LocalDateTime startTime;
    private String region;
    private String notice;
    private String chatRoomId;

    @ManyToMany
    @JoinTable(
            name = "meeting_users",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> participants;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingHashtagEntity> meetingHashtagEntities;

    private String imageUrl; // 이미지 URL 필드 추가

    public Meeting(String meetingTitle, CategoryEntity category) {
        this.title = meetingTitle;
        this.category = category;
    }
}

