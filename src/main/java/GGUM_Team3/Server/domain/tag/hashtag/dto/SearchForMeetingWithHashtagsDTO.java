package GGUM_Team3.Server.domain.tag.hashtag.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class SearchForMeetingWithHashtagsDTO {
    private String MeetingId;
    private String tempMeetingTitle;

    public SearchForMeetingWithHashtagsDTO(String MeetingId, String tempMeetingTitle) {
        this.MeetingId = MeetingId;
        this.tempMeetingTitle = tempMeetingTitle;
    }
}
