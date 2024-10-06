package cz.sinko.moneymanager.facade;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.CategoryDto;
import cz.sinko.moneymanager.facade.mapper.CategoryMapper;
import cz.sinko.moneymanager.repository.model.Category;
import cz.sinko.moneymanager.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Facade for account operations.
 *
 * @author Sinko Radovan
 */
@AllArgsConstructor
@Component
@Slf4j
public class CategoryFacade {

    private final CategoryService categoryService;

    /**
     * Create category.
     *
     * @param categoryDto category DTO
     * @return created category DTO
     */
    public CategoryDto createCategory(final CategoryDto categoryDto) {
        return CategoryMapper.t().map(categoryService.createCategory(categoryDto));
    }

    /**
     * Get all categories.
     *
     * @return list of categories
     */
    public List<CategoryDto> getCategories() {
        return CategoryMapper.t().map(categoryService.find().stream().sorted(Comparator.comparing(Category::getName)).toList());
    }

    /**
     * Update category.
     *
     * @param id category ID
     * @param categoryDto category DTO
     * @return updated category DTO
     * @throws ResourceNotFoundException if category was not found
     */
    public CategoryDto updateCategory(final Long id, final CategoryDto categoryDto) throws ResourceNotFoundException {
        return CategoryMapper.t().map(categoryService.updateCategory(id, categoryDto));
    }

    /**
     * Delete category.
     *
     * @param id category ID
     */
    public void deleteCategory(final Long id) {
        categoryService.deleteCategory(id);
    }
}
