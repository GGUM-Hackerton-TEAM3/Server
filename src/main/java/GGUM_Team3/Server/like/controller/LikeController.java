package GGUM_Team3.Server.like.controller;

import GGUM_Team3.Server.like.DTO.LikeDTO;
import GGUM_Team3.Server.like.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
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
