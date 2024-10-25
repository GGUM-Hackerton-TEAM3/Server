package GGUM_Team3.Server.domain.tag.hashtag.entity;

import GGUM_Team3.Server.domain.tempMeeting.entity.TempMeetingEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "meetingHashtag")
public class MeetingHashtagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID meetHashId;

    @ManyToOne
    @JoinColumn(name = "meetingId", nullable = false)
    @JsonBackReference
    private TempMeetingEntity tempMeetingEntity;

    @ManyToOne
    @JoinColumn(name = "hashtagId", nullable = false)
    private HashtagEntity hashtagEntity;

    public MeetingHashtagEntity(TempMeetingEntity tempMeetingEntity, HashtagEntity hashtagEntity) {
        this.tempMeetingEntity = tempMeetingEntity;
        this.hashtagEntity = hashtagEntity;
    }

}
