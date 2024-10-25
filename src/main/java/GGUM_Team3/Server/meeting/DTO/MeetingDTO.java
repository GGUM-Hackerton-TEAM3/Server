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
    private List<String> tags;
    private int likes; // 좋아요 수 필드 추가

    public static MeetingDTO fromEntity(Meeting meeting) {
        return MeetingDTO.builder()
                .id(meeting.getId()) // 모임 ID
                .creatorId(meeting.getCreatorId()) // 생성자 ID
                .title(meeting.getTitle()) // 제목 설정
                .description(meeting.getDescription()) // 설명 설정
                .maxParticipants(meeting.getMaxParticipants()) // 최대 참여자 수 설정
                .startTime(meeting.getStartTime()) // 시작 시간 설정
                .region(meeting.getRegion()) // 지역 설정
                .notice(meeting.getNotice()) // 공지 설정
                .chatRoomId(meeting.getChatRoomId()) // 채팅방 ID 설정
                .categoryId(meeting.getCategoryId()) // 카테고리 ID 설정
                .participants(meeting.getParticipants().stream() // 참여자 리스트
                        .map(UserDTO::fromEntity)
                        .collect(Collectors.toList()))
                .tags(meeting.getTags()) // 태그 리스트 설정
                .likes(meeting.getLikesCount()) // 좋아요 수 설정
                .build();
    }
}
