package practicum.utility.mappers;

import lombok.experimental.UtilityClass;
import practicum.model.Category;
import practicum.model.dto.CategoryDto;
import practicum.model.dto.NewCategoryDto;

import javax.validation.constraints.NotNull;

@UtilityClass
public class CategoryMapper {

    public Category fromNewCategoryDto(@NotNull NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
