package GGUM_Team3.Server.tag.hashtag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HashtagDTO {
    private UUID hashtagId;
    private String hashtagName;
}