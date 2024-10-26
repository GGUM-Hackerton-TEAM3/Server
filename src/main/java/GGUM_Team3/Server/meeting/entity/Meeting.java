// 모임 관련 엔티티
package GGUM_Team3.Server.meeting.entity;

import GGUM_Team3.Server.tag.category.entity.CategoryEntity;
import GGUM_Team3.Server.tag.hashtag.entity.MeetingHashtagEntity;
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
public class Meeting {
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
    private String region; // 지역 필드
    private String notice; // 공지 필드
    private String chatRoomId; // 채팅방ID 필드

    @ManyToMany
    @JoinTable(
            name = "meeting_members",
            joinColumns = @JoinColumn(name = "meeting_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> participants;


    // 해시태그와의 연관관계 추가
    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingHashtagEntity> meetingHashtagEntities;

    public Meeting(String meetingTitle, CategoryEntity category) {
        this.title = meetingTitle;
        this.category = category;
    }

}
