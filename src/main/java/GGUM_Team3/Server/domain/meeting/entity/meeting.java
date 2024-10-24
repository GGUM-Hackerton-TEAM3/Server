package GGUM_Team3.Server.domain.meeting.entity;


import GGUM_Team3.Server.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "meeting")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID )
    @Column(name = "모임ID", nullable = false)
    private UUID id;

    @Column(name = "카테고리ID", nullable = false)
    private int categoryId;

    @Column(name = "모임이름", nullable = false, length = 100)
    private String name;

    @Column(name = "모임설명", columnDefinition = "TEXT")
    private String description;

    @Column(name = "모임최대인원")
    private Integer maxParticipants;

    @Column(name = "모임생성시간")
    private Timestamp createdTime;

    @Column(name = "지역", length = 255)
    private String location;

    @Column(name = "공지", columnDefinition = "TEXT")
    private String announcement;

    @Column(name = "채팅방ID")
    private UUID chatRoomId;

    @ManyToOne
    @JoinColumn(name = "생성자ID", nullable = false)
    private UserEntity creator;  // User와의 연관관계
}

