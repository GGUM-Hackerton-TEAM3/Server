package GGUM_Team3.Server.domain.tempMeeting.entity;

import GGUM_Team3.Server.domain.tag.hashtag.entity.MeetingHashtagEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tempMeeting")
public class TempMeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID tempMeetingId;

    @Column(name = "tempMeetingTitle", nullable = false, unique = true)
    private String tempMeetingTitle;

    @OneToMany(mappedBy = "tempMeetingEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MeetingHashtagEntity> meetingHashtagEntities;

}
