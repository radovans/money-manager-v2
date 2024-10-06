package cz.sinko.moneymanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.sinko.moneymanager.repository.model.Category;
import cz.sinko.moneymanager.repository.model.Subcategory;


/**
 * Repository for Subcategory entity.
 *
 * @author Sinko Radovan
 */
@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    /**
     * Find subcategory by name.
     *
     * @param category name of subcategory
     * @return subcategory
     */
    Optional<Subcategory> findByName(final String category);

    /**
     * Find subcategories by category.
     *
     * @param category category
     * @return list of subcategories
     */
    List<Subcategory> findByCategory(final Category category);
}
