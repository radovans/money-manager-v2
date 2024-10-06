package cz.sinko.moneymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.sinko.moneymanager.repository.model.Rule;


/**
 * Repository for Rule entity.
 *
 * @author Sinko Radovan
 */
@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

}
