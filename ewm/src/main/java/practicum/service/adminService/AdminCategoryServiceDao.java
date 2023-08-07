package practicum.service.adminService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practicum.model.dto.NewCategoryDto;
import practicum.utility.mappers.CategoryMapper;
import practicum.model.Category;
import practicum.model.dto.CategoryDto;
import practicum.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceDao implements AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.fromNewCategoryDto(newCategoryDto)));
    }

    @Override
    @Transactional
    public CategoryDto patchCategory(long catId, NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.findCategoryById(catId);
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(long catId) {
        categoryRepository.deleteById(catId);
    }
}
