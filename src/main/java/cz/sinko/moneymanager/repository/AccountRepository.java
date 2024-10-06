package cz.sinko.moneymanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.sinko.moneymanager.repository.model.Account;


/**
 * Repository for Account entity.
 *
 * @author Sinko Radovan
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find account by name.
     *
     * @param account name of account
     * @return account
     */
    Optional<Account> findByName(final String account);
}
