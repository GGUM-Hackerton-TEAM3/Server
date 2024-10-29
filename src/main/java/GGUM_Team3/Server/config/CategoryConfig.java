package GGUM_Team3.Server.config;

import GGUM_Team3.Server.domain.tag.category.entity.CategoryEntity;
import GGUM_Team3.Server.domain.tag.category.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CategoryConfig {

    @Bean
    CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            List<CategoryEntity> categories = Arrays.asList(
                    new CategoryEntity("Movie"), // 1
                    new CategoryEntity("Performance/Art"), // 2
                    new CategoryEntity("Exercise"), // 3
                    new CategoryEntity("Food"), // 4
                    new CategoryEntity("Self-Development"), // 5
                    new CategoryEntity("Photography/Video"), // 6
                    new CategoryEntity("Book/Writing"), // 7
                    new CategoryEntity("Game/Entertainment") // 8
            );

            // 중복 생성 방지
            categories.forEach(category -> {
                if (!categoryRepository.existsById(category.getCategoryId())) {
                    categoryRepository.save(category);
                }
            });
        };
    }
}
