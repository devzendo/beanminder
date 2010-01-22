package org.devzendo.simpleaccounts.persistence;

import org.devzendo.minimiser.persistence.DAOFactory;
import org.devzendo.simpleaccounts.persistence.dao.AccountsDao;
import org.devzendo.simpleaccounts.persistence.dao.TransactionsDao;


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
