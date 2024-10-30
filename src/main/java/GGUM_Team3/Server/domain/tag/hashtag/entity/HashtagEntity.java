package GGUM_Team3.Server.domain.tag.hashtag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hashtag")
public class HashtagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID hashtagId;

    @Column(unique = true, nullable = false)
    private String hashtagName;

    @OneToMany(mappedBy = "hashtagEntity")
    @JsonIgnore // 순환참조 문제해결
    private List<MeetingHashtagEntity> meetingHashtagEntities;

    public HashtagEntity(String hashtagName) {
        this.hashtagName = hashtagName;
    }

}
