package GGUM_Team3.Server.domain.tag.category.controller;

import GGUM_Team3.Server.domain.meeting.dto.MeetingDTO;
import GGUM_Team3.Server.domain.meeting.entity.Meeting;
import GGUM_Team3.Server.domain.tag.category.dto.CategoryDTO;
import GGUM_Team3.Server.domain.tag.category.entity.CategoryEntity;
import GGUM_Team3.Server.domain.tag.category.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "카테고리")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        try {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "모든 카테고리 조회 중 오류가 발생했습니다.", e);
        }
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable int categoryId) {
        try {
            CategoryDTO category = categoryService.getCategoryById(categoryId);
            if (category != null) {
                return ResponseEntity.ok(category);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 ID로 조회 중 오류가 발생했습니다.", e);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 삭제 중 오류가 발생했습니다.", e);
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createOrGetCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            CategoryEntity category = categoryService.createOrGetCategory(categoryDTO.getCategoryName());
            CategoryDTO responseCategory = new CategoryDTO(category.getCategoryId(), category.getCategoryName());
            return ResponseEntity.ok(responseCategory);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카테고리 생성 중 오류가 발생했습니다.", e);
        }
    }

    @GetMapping("/search/meetings")
    public ResponseEntity<List<MeetingDTO>> searchForMeetingWithCategory(@RequestParam String categoryName) {
        try {
            List<Meeting> meetings = categoryService.searchForMeetingWithCategory(categoryName);

            // Meeting 리스트를 MeetingDTO 리스트로 변환
            List<MeetingDTO> meetingDTOs = meetings.stream()
                    .map(MeetingDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(meetingDTOs);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "카테고리로 미팅 검색 중 오류가 발생했습니다.", e);
        }
    }
}

