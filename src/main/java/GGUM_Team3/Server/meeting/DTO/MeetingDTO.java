// 모임 DTO
package GGUM_Team3.Server.meeting.DTO;

import GGUM_Team3.Server.domain.user.dto.UserDTO;
import GGUM_Team3.Server.meeting.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDTO {
    private String id;
    private String creatorId;
    private String title;
    private String description;
    private int maxParticipants;
    private LocalDateTime startTime;
    private String region; // 지역 필드 추가
    private String notice; // 공지 필드 추가
    private String chatRoomId; // 채팅방ID 필드 추가
    private String categoryId; // 카테고리ID 필드 추가
    private List<UserDTO> participants;

    public static MeetingDTO fromEntity(Meeting meeting) {
        return MeetingDTO.builder()
                .id(meeting.getId())
                .creatorId(meeting.getCreatorId())
                .title(meeting.getTitle())
                .description(meeting.getDescription())
                .maxParticipants(meeting.getMaxParticipants())
                .startTime(meeting.getStartTime())
                .region(meeting.getRegion())
                .notice(meeting.getNotice())
                .chatRoomId(meeting.getChatRoomId())
                .categoryId(meeting.getCategoryId())
                .participants(meeting.getParticipants().stream()
                        .map(UserDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}

