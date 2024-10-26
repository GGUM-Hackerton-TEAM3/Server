package GGUM_Team3.Server.tempMeeting.controller;

import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")  // 테스트 전용 프로파일 사용
@Disabled
public class TempMeetingControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private TempMeetingService tempMeetingService;
//
//    @MockBean
//    private MeetingHashtagService meetingHashtagService;  // 추가된 MockBean
//
//    @MockBean
//    private HashtagService hashtagService;
//
//    // -------------------------- config관련 MockBean처리 --------------------------
//    @MockBean
//    private S3Config s3Config;
//
//    @MockBean
//    private SwaggerConfig swaggerConfig;
//
//    @MockBean
//    private WebSecurityConfig webSecurityConfig;
//    // ---------------------------------------------------------------------------
//
//
//    // ------------------------- Security관련 MockBean처리 -------------------------
//    @MockBean
//    private GoogleTokenVerifier googleTokenVerifier;
//
//    @MockBean
//    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
//
//    @MockBean
//    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    @MockBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @MockBean
//    private TokenProvider tokenProvider;
//    // ---------------------------------------------------------------------------
//
//
//    // --------------- S3config에 사용하는 관련 서비스도 MocekBean처리 ----------------
//    @MockBean
//    private ImageService imageService;
//    // ---------------------------------------------------------------------------
//
//
//    // -------------- 로그인에서만 필요한 레포지토리와 인코더를 Mock 처리 ----------------
//    @MockBean
//    private GGUM_Team3.Server.domain.user.repository.UserRepository userRepository;
//
//    @MockBean
//    private PasswordEncoder passwordEncoder;  // MockBean으로 PasswordEncoder 추가
//    // ---------------------------------------------------------------------------
//
//
//    // 해쉬태그 생성 테스트 코드
//    @Test
//    public void createMeeting_ShouldInsertHashtagsCorrectly() throws Exception {
//        // Given: 미팅 DTO 생성
//        List<String> hashtags = Arrays.asList("hashtag1", "hashtag2", "hashtag3");
//        TempMeetingDTO tempMeetingDTO = TempMeetingDTO.builder()
//                .tempMeetingTitle("Test Meeting")
//                .hashtags(hashtags)
//                .build();
//
//        // TempMeetingEntity 객체 생성 및 Mocking 설정
//        TempMeetingEntity savedMeeting = new TempMeetingEntity();
//        savedMeeting = TempMeetingEntity.builder()
//                .tempMeetingTitle("Test Meeting")
//                .meetingHashtagEntities(Arrays.asList(
//                        new MeetingHashtagEntity(savedMeeting, new HashtagEntity("hashtag1")),
//                        new MeetingHashtagEntity(savedMeeting, new HashtagEntity("hashtag2")),
//                        new MeetingHashtagEntity(savedMeeting, new HashtagEntity("hashtag3"))
//                ))
//                .build();
//
//        // 서비스 계층의 동작을 Mock 설정
//        when(tempMeetingService.createMeeting(any(TempMeetingDTO.class))).thenReturn(savedMeeting);
//
//        // When: API 호출
//
//        // 모임 만들기
//        MvcResult testResult_CreateMeeting = mockMvc.perform(post("/bungae/meetings/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(tempMeetingDTO)))
//                // Then: 응답 상태 및 값 확인
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.tempMeetingTitle").value("Test Meeting"))
//                .andExpect(jsonPath("$.meetingHashtagEntities[0].hashtagEntity.hashtagName").value("hashtag1"))
//                .andExpect(jsonPath("$.meetingHashtagEntities[1].hashtagEntity.hashtagName").value("hashtag2"))
//                .andExpect(jsonPath("$.meetingHashtagEntities[2].hashtagEntity.hashtagName").value("hashtag3"))
//                .andReturn();
//
//        // 결과 출력
//        String jsonResponseCreateMeeting = testResult_CreateMeeting.getResponse().getContentAsString();
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------");
//        System.out.println("응답 내용: " + jsonResponseCreateMeeting);  // 출력
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------\n");
//
//        // 중복 제목으로 새로운 DTO 생성 후 API 호출
//        TempMeetingDTO duplicateMeetingDTO = new TempMeetingDTO();
//        duplicateMeetingDTO.setTempMeetingTitle("Test Meeting");
//        duplicateMeetingDTO.setHashtags(Arrays.asList("hashtag1", "hashtag2", "hashtag3"));
//
//        // 모임 만들기 - 제목 중복 테스트
//        mockMvc.perform(post("/bungae/meetings/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(duplicateMeetingDTO)))
//                .andExpect(status().isConflict())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException))
//                .andExpect(result -> assertEquals("Meeting title already exists", result.getResolvedException().getMessage()));
//
//        // 결과 출력
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------");
//        System.out.println("중복된 제목으로 인해 예외가 발생했습니다.");
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------\n");
//    }
//
//    // 해쉬태그 추가 테스트 코드
//    @Test
//    public void addHashtagsToMeeting_ShouldAddHashtagsSuccessfully() throws Exception {
//        // Given: 미팅 DTO 생성
//        List<String> hashtags = Arrays.asList("newHashtag1", "newHashtag2");
//        TempMeetingDTO tempMeetingDTO = TempMeetingDTO.builder()
//                .tempMeetingTitle("Test Meeting")
//                .hashtags(hashtags)
//                .build();
//
//        // TempMeetingEntity 객체 생성 및 Mocking 설정
//        TempMeetingEntity existingMeeting = TempMeetingEntity.builder()
//                .tempMeetingTitle("Test Meeting")
//                .meetingHashtagEntities(new ArrayList<>())
//                .build();
//
//        TempMeetingEntity updatedMeeting = TempMeetingEntity.builder()
//                .tempMeetingTitle("Test Meeting")
//                .meetingHashtagEntities(Arrays.asList(
//                        new MeetingHashtagEntity(existingMeeting, new HashtagEntity("newHashtag1")),
//                        new MeetingHashtagEntity(existingMeeting, new HashtagEntity("newHashtag2"))
//                ))
//                .build();
//
//        // 서비스 계층의 동작을 Mock 설정
//        when(tempMeetingService.getMeetingByTitle("Test Meeting")).thenReturn(existingMeeting);
//        doReturn(ResponseEntity.ok(updatedMeeting))
//                .when(meetingHashtagService).addHashtagsToMeeting(any(TempMeetingEntity.class), anyList());
//
//        // When: API 호출
//        MvcResult testResult_AddHashtags = mockMvc.perform(post("/bungae/meetings/addHashtags")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(tempMeetingDTO)))
//                // Then: 응답 상태 및 값 확인
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.tempMeetingTitle").value("Test Meeting"))
//                .andExpect(jsonPath("$.meetingHashtagEntities[0].hashtagEntity.hashtagName").value("newHashtag1"))
//                .andExpect(jsonPath("$.meetingHashtagEntities[1].hashtagEntity.hashtagName").value("newHashtag2"))
//                .andReturn();
//
//        // 결과 출력
//        String jsonResponseAddHashtags = testResult_AddHashtags.getResponse().getContentAsString();
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------");
//        System.out.println("응답 내용: " + jsonResponseAddHashtags);  // 출력
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------\n");
//    }
//
//    // 해시태그 수정 테스트 코드
//    @Test
//    public void updateHashtagsForMeeting_ShouldUpdateHashtagsSuccessfully() throws Exception {
//        // Given: 기존 미팅 DTO 생성
//        List<String> originalHashtags = Arrays.asList("hashtag1", "hashtag2");
//        TempMeetingDTO tempMeetingDTO = TempMeetingDTO.builder()
//                .tempMeetingTitle("Test Meeting")
//                .hashtags(originalHashtags)
//                .build();
//
//        // TempMeetingEntity 객체 생성 및 Mocking 설정 (기존 해시태그 포함)
//        TempMeetingEntity existingMeeting = TempMeetingEntity.builder()
//                .tempMeetingTitle("Test Meeting")
//                .meetingHashtagEntities(Arrays.asList(
//                        new MeetingHashtagEntity(null, new HashtagEntity("hashtag1")),
//                        new MeetingHashtagEntity(null, new HashtagEntity("hashtag2"))
//                ))
//                .build();
//
//        // 수정된 해시태그 DTO 생성
//        List<String> updatedHashtags = Arrays.asList("updatedHashtag1", "updatedHashtag2");
//        TempMeetingDTO updatedMeetingDTO = TempMeetingDTO.builder()
//                .tempMeetingTitle("Test Meeting")
//                .hashtags(updatedHashtags)
//                .build();
//
//        // TempMeetingEntity 객체 생성 및 Mocking 설정 (수정된 해시태그 포함)
//        TempMeetingEntity updatedMeeting = TempMeetingEntity.builder()
//                .tempMeetingTitle("Test Meeting")
//                .meetingHashtagEntities(Arrays.asList(
//                        new MeetingHashtagEntity(null, new HashtagEntity("updatedHashtag1")),
//                        new MeetingHashtagEntity(null, new HashtagEntity("updatedHashtag2"))
//                ))
//                .build();
//
//        // 서비스 계층의 동작을 Mock 설정
//        when(tempMeetingService.getMeetingByTitle("Test Meeting")).thenReturn(existingMeeting);
//        doReturn(ResponseEntity.ok(updatedMeeting))
//                .when(meetingHashtagService).updateHashtagsForMeeting(any(TempMeetingEntity.class), anyList());
//
//        // When: API 호출
//        MvcResult testResult_UpdateHashtags = mockMvc.perform(put("/bungae/meetings/updateHashtags")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedMeetingDTO)))
//                // Then: 응답 상태 및 값 확인
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.tempMeetingTitle").value("Test Meeting"))
//                .andExpect(jsonPath("$.meetingHashtagEntities[0].hashtagEntity.hashtagName").value("updatedHashtag1"))
//                .andExpect(jsonPath("$.meetingHashtagEntities[1].hashtagEntity.hashtagName").value("updatedHashtag2"))
//                .andReturn();
//
//        // 결과 출력
//        String jsonResponseUpdateHashtags = testResult_UpdateHashtags.getResponse().getContentAsString();
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------");
//        System.out.println("응답 내용: " + jsonResponseUpdateHashtags);  // 출력
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------\n");
//    }
//
//    // 해시태그로 미팅 검색 테스트 코드
//    @Test
//    public void searchMeetingsByHashtag_ShouldReturnCorrectMeetings() throws Exception {
//        // Given: 해시태그 이름과 관련된 미팅들 설정
//        String hashtag = "TestHashtag";
//        TempMeetingEntity tempMeeting1 = TempMeetingEntity.builder()
//                .tempMeetingTitle("Unique Meeting 1")
//                .build();
//
//        TempMeetingEntity tempMeeting2 = TempMeetingEntity.builder()
//                .tempMeetingTitle("Unique Meeting 2")
//                .build();
//
//        HashtagEntity hashtagEntity = HashtagEntity.builder()
//                .hashtagName(hashtag)
//                .build();
//
//        tempMeeting1.setMeetingHashtagEntities(Arrays.asList(
//                MeetingHashtagEntity.builder()
//                        .tempMeetingEntity(tempMeeting1)
//                        .hashtagEntity(hashtagEntity)
//                        .build()
//        ));
//
//        tempMeeting2.setMeetingHashtagEntities(Arrays.asList(
//                MeetingHashtagEntity.builder()
//                        .tempMeetingEntity(tempMeeting2)
//                        .hashtagEntity(hashtagEntity)
//                        .build()
//        ));
//
//        List<TempMeetingEntity> meetings = Arrays.asList(tempMeeting1, tempMeeting2);
//        // meetings 리스트를 DTO로 변환
//        List<SearchForMeetingWithHashtagsDTO> meetingDTOs = meetings.stream()
//                .map(meeting -> SearchForMeetingWithHashtagsDTO.builder()
//                        .tempMeetingId(meeting.getTempMeetingId())
//                        .tempMeetingTitle(meeting.getTempMeetingTitle())
//                        .build())
//                .collect(Collectors.toList());
//
//        // HashtagMeetingsDTO 생성
//        HashtagMeetingsDTO expectedResponse = new HashtagMeetingsDTO(hashtag, meetingDTOs);
//
//
//        // 서비스 계층의 동작을 Mock 설정
//        when(meetingHashtagService.findMeetingsByHashtag(hashtag)).thenReturn(expectedResponse);
//
//        // When: API 호출
//        MvcResult testResult_SearchMeetings = mockMvc.perform(get("/api/hashtags/search")
//                        .param("hashtag", hashtag)
//                        .contentType(MediaType.APPLICATION_JSON))
//                // Then: 응답 상태 및 값 확인
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.hashtagName").value(hashtag))
//                .andExpect(jsonPath("$.tempMeetings[0].tempMeetingTitle").value("Unique Meeting 1"))
//                .andExpect(jsonPath("$.tempMeetings[1].tempMeetingTitle").value("Unique Meeting 2"))
//                .andReturn();
//
//        // 결과 출력
//        String jsonResponseSearchMeetings = testResult_SearchMeetings.getResponse().getContentAsString();
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------");
//        System.out.println("응답 내용: " + jsonResponseSearchMeetings);  // 출력
//        System.out.println("\n------------------------------------------------- 로그 응답 내용 -------------------------------------------------\n");
//    }


}
