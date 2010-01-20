package uk.me.gumbley.simpleaccounts.persistence;

import org.devzendo.minimiser.persistence.DAOFactory;

import uk.me.gumbley.simpleaccounts.persistence.dao.AccountsDao;
import uk.me.gumbley.simpleaccounts.persistence.dao.TransactionsDao;

/**
 * The DAO Factory for accessing the SimpleAccounts database.
 * @author matt
 *
 */
public interface SimpleAccountsDAOFactory extends DAOFactory {
    /**
     * @return the AccountsDao
     */
    AccountsDao getAccountsDao();

    /**
     * @return the TransactionsDao
     */
    TransactionsDao getTransactionsDao();
}
