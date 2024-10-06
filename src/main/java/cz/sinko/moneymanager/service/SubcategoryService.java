package cz.sinko.moneymanager.service;

import java.util.List;

import org.springframework.stereotype.Component;

import cz.sinko.moneymanager.config.exception.ResourceNotFoundException;
import cz.sinko.moneymanager.facade.dto.SubcategoryDto;
import cz.sinko.moneymanager.repository.SubcategoryRepository;
import cz.sinko.moneymanager.repository.model.Category;
import cz.sinko.moneymanager.repository.model.Subcategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Service for subcategory operations.
 *
 * @author Sinko Radovan
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;

    /**
     * Create subcategory.
     *
     * @param subcategory subcategory
     * @param category category
     * @return created subcategory
     */
    public Subcategory createSubcategory(final Subcategory subcategory, final Category category) {
        subcategory.setCategory(category);
        return subcategoryRepository.save(subcategory);
    }

    /**
     * Find subcategory by ID.
     *
     * @param subcategoryId subcategory ID
     * @return subcategory
     * @throws ResourceNotFoundException if subcategory was not found
     */
    public Subcategory find(final Long subcategoryId) throws ResourceNotFoundException {
        return subcategoryRepository.findById(subcategoryId).orElseThrow(() -> ResourceNotFoundException.createWith(
            "Subcategory",
            " with id '" + subcategoryId + "' was not found"));
    }

    /**
     * Find subcategory by name.
     *
     * @param subcategory subcategory name
     * @return subcategory
     * @throws ResourceNotFoundException if subcategory was not found
     */
    public Subcategory find(final String subcategory) throws ResourceNotFoundException {
        return subcategoryRepository.findByName(subcategory).orElseThrow(() -> ResourceNotFoundException.createWith(
            "Subcategory",
            " with name '" + subcategory + "' was not found"));
    }

    /**
     * Find all subcategories.
     *
     * @return list of subcategories
     */
    public List<Subcategory> findAll() {
        return subcategoryRepository.findAll();
    }

    /**
     * Find subcategories by category.
     *
     * @param category category
     * @return list of subcategories
     */
    public List<Subcategory> findByCategory(final Category category) {
        return subcategoryRepository.findByCategory(category);
    }

    /**
     * Update subcategory.
     *
     * @param id subcategory ID
     * @param subcategoryDto subcategory DTO
     * @param newCategory new category
     * @return updated subcategory
     * @throws ResourceNotFoundException if subcategory was not found
     */
    public Subcategory updateSubcategory(final Long id, final SubcategoryDto subcategoryDto, final Category newCategory)
        throws ResourceNotFoundException {
        final Subcategory subcategory = find(id);
        subcategory.setName(subcategoryDto.getName());
        if (subcategory.getCategory() != null && subcategoryDto.getCategory() != null
            && !subcategory.getCategory().getName().equals(subcategoryDto.getCategory())) {
            subcategory.setCategory(newCategory);
        }
        return subcategoryRepository.save(subcategory);
    }

    /**
     * Delete subcategory.
     *
     * @param id subcategory ID
     */
    public void deleteSubcategory(final Long id) {
        subcategoryRepository.deleteById(id);
    }
}
