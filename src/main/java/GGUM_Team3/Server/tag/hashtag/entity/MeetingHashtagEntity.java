package GGUM_Team3.Server.tag.hashtag.entity;

import GGUM_Team3.Server.meeting.entity.Meeting;
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
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "hashtagId", nullable = false)
    private HashtagEntity hashtagEntity;

    public MeetingHashtagEntity(Meeting meeting, HashtagEntity hashtagEntity) {
        this.meeting = meeting;
        this.hashtagEntity = hashtagEntity;
    }

}
