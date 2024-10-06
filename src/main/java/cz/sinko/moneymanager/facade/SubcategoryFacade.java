package cz.sinko.moneymanager.facade;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.SubcategoryDto;
import cz.sinko.moneymanager.facade.mapper.SubcategoryMapper;
import cz.sinko.moneymanager.repository.model.Category;
import cz.sinko.moneymanager.repository.model.Subcategory;
import cz.sinko.moneymanager.service.CategoryService;
import cz.sinko.moneymanager.service.SubcategoryService;
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
public class SubcategoryFacade {

    private final SubcategoryService subcategoryService;

    private final CategoryService categoryService;

    /**
     * Create subcategory.
     *
     * @param subcategoryDto subcategory DTO
     * @return created subcategory DTO
     * @throws ResourceNotFoundException if category was not found
     */
    public SubcategoryDto createSubcategory(final SubcategoryDto subcategoryDto) throws ResourceNotFoundException {
        final Subcategory subcategory = SubcategoryMapper.t().map(subcategoryDto);
        final Category category = categoryService.find(subcategoryDto.getCategory());
        return SubcategoryMapper.t().map(subcategoryService.createSubcategory(subcategory, category));
    }

    /**
     * Get all subcategories.
     *
     * @param category category name
     * @return list of subcategories
     * @throws ResourceNotFoundException if category was not found
     */
    public List<SubcategoryDto> getSubcategories(final String category) throws ResourceNotFoundException {
        if (category != null && !category.isBlank()) {
            final Category categoryEntity = categoryService.find(category);
            return SubcategoryMapper.t()
                .map(subcategoryService.findByCategory(categoryEntity)
                    .stream()
                    .sorted(Comparator.comparing(Subcategory::getName))
                    .collect(Collectors.toList()));
        } else {
            return SubcategoryMapper.t()
                .map(subcategoryService.findAll().stream().sorted(Comparator.comparing(Subcategory::getName)).collect(Collectors.toList()));
        }
    }

    /**
     * Update subcategory.
     *
     * @param id subcategory ID
     * @param subcategoryDto subcategory DTO
     * @return updated subcategory DTO
     * @throws ResourceNotFoundException if subcategory or category was not found
     */
    public SubcategoryDto updateSubcategory(final Long id, final SubcategoryDto subcategoryDto) throws ResourceNotFoundException {
        final Category newCategory = categoryService.find(subcategoryDto.getCategory());
        final Subcategory oldSubcategory = subcategoryService.find(id);
        return SubcategoryMapper.t().map(subcategoryService.updateSubcategory(id, subcategoryDto, newCategory));
    }

    /**
     * Delete subcategory.
     *
     * @param id subcategory ID
     */
    public void deleteSubcategory(final Long id) {
        subcategoryService.deleteSubcategory(id);
    }
}
