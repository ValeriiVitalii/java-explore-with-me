package practicum.service.adminService;


import practicum.model.CategoryDto;
import practicum.model.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    CategoryDto patchCategory(long catId, NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);
}