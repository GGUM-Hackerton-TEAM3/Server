package GGUM_Team3.Server.domain.tempMeeting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempMeetingDTO {
    private UUID tempMeetingId;
    private String tempMeetingTitle;
    private List<String> hashtags;
}
