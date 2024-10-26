package GGUM_Team3.Server.tag.category.repository;


import GGUM_Team3.Server.meeting.entity.Meeting;
import GGUM_Team3.Server.tag.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findByCategoryName(String CategoryName);

    Optional<CategoryEntity> findByCategoryId(Integer categoryId);
}
