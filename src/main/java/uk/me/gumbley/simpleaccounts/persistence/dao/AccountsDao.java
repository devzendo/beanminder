package uk.me.gumbley.simpleaccounts.persistence.dao;

import java.util.List;

import uk.me.gumbley.simpleaccounts.persistence.domain.Account;

/**
 * The Data Access Object for Accounts.
 *
 * @author matt
 *
 */
public interface AccountsDao {
    /**
     * Find all the accounts sorted by name.
     * @return all the accounts, sorted on name.
     */
    List<Account> findAllAccounts();

    /**
     * Save (insert or update) an account.
     * @param account the account to insert or update
     * @return the saved account; if inserted, the id will contain
     * the primary key.
     */
    Account saveAccount(Account account);

    /**
     * Delete an account, cascading the deletion of all its
     * transactions.
     * @param account the account to delete
     */
    void deleteAccount(Account account);
}
