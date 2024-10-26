package GGUM_Team3.Server.tag.category.service;

import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.tag.category.dto.CategoryDTO;
import GGUM_Team3.Server.tag.category.repository.CategoryRepository;
import GGUM_Team3.Server.tag.category.entity.CategoryEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDTO> getAllCategories() {
        try {
            return categoryRepository.findAll().stream()
                    .map(category -> new CategoryDTO(category.getCategoryId(), category.getCategoryName()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("모든 카테고리 조회 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("모든 카테고리 조회 중 오류가 발생했습니다.", e);
        }
    }

    public CategoryDTO getCategoryById(Integer categoryId) {
        try {
            Optional<CategoryEntity> category = categoryRepository.findByCategoryId(categoryId);
            return category.map(cat -> new CategoryDTO(cat.getCategoryId(), cat.getCategoryName())).orElse(null);
        } catch (Exception e) {
            log.error("카테고리 ID로 조회 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("카테고리 조회 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteCategory(Integer categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
        } catch (Exception e) {
            log.error("카테고리 삭제 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("카테고리 삭제 중 오류가 발생했습니다.", e);
        }
    }

    public CategoryEntity createOrGetCategory(String categoryName) {
        try {
            return categoryRepository.findByCategoryName(categoryName)
                    .orElseGet(() -> categoryRepository.save(new CategoryEntity(categoryName)));
        } catch (Exception e) {
            log.error("카테고리 생성 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("카테고리 생성 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Meeting> searchForMeetingWithCategory(String categoryName) {
        try {
            CategoryEntity category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
            return category.getMeetings();
        } catch (Exception e) {
            log.error("카테고리로 미팅 검색 중 오류가 발생했습니다: {}", e.getMessage(), e);
            throw new RuntimeException("카테고리로 미팅 검색 중 오류가 발생했습니다.", e);
        }
    }

}
