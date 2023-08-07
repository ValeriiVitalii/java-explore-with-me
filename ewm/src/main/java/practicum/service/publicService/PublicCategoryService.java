package practicum.service.publicService;

import org.springframework.data.domain.Pageable;
import practicum.model.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getCategory(Pageable pageable);

    CategoryDto getCategoryById(long catId);

}
