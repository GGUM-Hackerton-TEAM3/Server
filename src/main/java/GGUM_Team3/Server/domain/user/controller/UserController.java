package GGUM_Team3.Server.domain.user.controller;

import GGUM_Team3.Server.domain.user.dto.UserProfileResponse;
import GGUM_Team3.Server.domain.user.dto.UserProfileUpdateRequest;
import GGUM_Team3.Server.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "마이페이지")
public class UserController {
    private final UserService userService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@AuthenticationPrincipal String userId) {
        UserProfileResponse profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "내 정보 수정", description = "UserProfileResponse 반한")
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @AuthenticationPrincipal String userId,
            @ModelAttribute UserProfileUpdateRequest updateRequest) {
        UserProfileResponse updatedProfile = userService.updateUserProfile(userId, updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }
}
