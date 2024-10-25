package GGUM_Team3.Server.domain.tag.hashtag.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class SearchForMeetingWithHashtagsDTO {
    private UUID tempMeetingId;
    private String tempMeetingTitle;

    public SearchForMeetingWithHashtagsDTO(UUID tempMeetingId, String tempMeetingTitle) {
        this.tempMeetingId = tempMeetingId;
        this.tempMeetingTitle = tempMeetingTitle;
    }
}
