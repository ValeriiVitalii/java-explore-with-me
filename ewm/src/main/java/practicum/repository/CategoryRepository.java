package practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practicum.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findCategoryById(long catId);

}