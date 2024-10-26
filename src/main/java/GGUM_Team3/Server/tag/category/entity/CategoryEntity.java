package GGUM_Team3.Server.tag.category.entity;

import GGUM_Team3.Server.meeting.entity.Meeting;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int categoryId;

    @Column(nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Meeting> meetings;

    public CategoryEntity(String categoryName) {
        this.categoryName = categoryName;
    }

    public CategoryEntity(String categoryName, List<Meeting> meetings) {
        this.categoryName = categoryName;
        this.meetings = meetings;
    }

}
