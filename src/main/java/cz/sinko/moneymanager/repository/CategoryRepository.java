package cz.sinko.moneymanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.sinko.moneymanager.repository.model.Category;


/**
 * Repository for Category entity.
 *
 * @author Sinko Radovan
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find category by name.
     *
     * @param category name of the category
     * @return category
     */
    Optional<Category> findByName(String category);
}
