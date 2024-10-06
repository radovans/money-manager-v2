package cz.sinko.moneymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.sinko.moneymanager.repository.model.Rule;
import cz.sinko.moneymanager.repository.model.StatementSetting;


/**
 * Repository for Statement Setting entity.
 *
 * @author Sinko Radovan
 */
@Repository
public interface StatementSettingRepository extends JpaRepository<StatementSetting, Long> {

}
