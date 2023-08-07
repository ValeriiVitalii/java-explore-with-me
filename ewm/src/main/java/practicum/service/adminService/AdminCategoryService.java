package practicum.service.adminService;


import practicum.model.dto.CategoryDto;
import practicum.model.dto.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    CategoryDto patchCategory(long catId, NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);
}