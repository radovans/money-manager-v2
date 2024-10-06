package cz.sinko.moneymanager.service;

import java.util.List;

import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.CategoryDto;
import cz.sinko.moneymanager.facade.mapper.CategoryMapper;
import cz.sinko.moneymanager.repository.CategoryRepository;
import cz.sinko.moneymanager.repository.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Service for category operations.
 *
 * @author Sinko Radovan
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Create category.
     *
     * @param categoryDto category DTO
     * @return created category
     */
    public Category createCategory(final CategoryDto categoryDto) {
        final Category category = CategoryMapper.t().map(categoryDto);
        return categoryRepository.save(category);
    }

    /**
     * Find category by ID.
     *
     * @param categoryId category ID
     * @return category
     * @throws ResourceNotFoundException if category was not found
     */
    public Category find(final Long categoryId) throws ResourceNotFoundException {
        return categoryRepository.findById(categoryId).orElseThrow(() -> ResourceNotFoundException.createWith(
            "Category",
            " with id '" + categoryId + "' was not found"));
    }

    /**
     * Find category by name.
     *
     * @param category category name
     * @return category
     * @throws ResourceNotFoundException if category was not found
     */
    public Category find(final String category) throws ResourceNotFoundException {
        return categoryRepository.findByName(category).orElseThrow(() -> ResourceNotFoundException.createWith(
            "Category",
            " with name '" + category + "' was not found"));
    }

    /**
     * Find all categories.
     *
     * @return list of categories
     */
    public List<Category> find() {
        return categoryRepository.findAll();
    }

    /**
     * Update category.
     *
     * @param id category ID
     * @param categoryDto category DTO
     * @return updated category
     * @throws ResourceNotFoundException if category was not found
     */
    public Category updateCategory(final Long id, final CategoryDto categoryDto) throws ResourceNotFoundException {
        final Category category = find(id);
        category.setName(categoryDto.getName());
        return categoryRepository.save(category);
    }

    /**
     * Delete category.
     *
     * @param id category ID
     */
    public void deleteCategory(final Long id) {
        categoryRepository.deleteById(id);
    }
}
