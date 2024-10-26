package GGUM_Team3.Server.tag.hashtag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashtagMeetingsDTO {
    private String hashtagName;
    private List<SearchForMeetingWithHashtagsDTO> tempMeetings;
}
