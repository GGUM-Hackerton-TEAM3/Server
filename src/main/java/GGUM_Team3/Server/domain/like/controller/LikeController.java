package GGUM_Team3.Server.domain.like.controller;

import GGUM_Team3.Server.domain.like.dto.LikeDTO;
import GGUM_Team3.Server.domain.like.service.LikeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@Tag(name = "좋아요")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like")
    public ResponseEntity<String> likeMeeting(@RequestBody LikeDTO likeDTO) {
        likeService.likeMeeting(likeDTO);
        return ResponseEntity.ok("Meeting liked successfully.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/unlike")
    public ResponseEntity<String> unlikeMeeting(@RequestBody LikeDTO likeDTO) {
        likeService.unlikeMeeting(likeDTO);
        return ResponseEntity.ok("Meeting unliked successfully.");
    }
}
