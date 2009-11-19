package uk.me.gumbley.simpleaccounts.persistence.impl;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import uk.me.gumbley.simpleaccounts.persistence.SimpleAccountsDAOFactory;
import uk.me.gumbley.simpleaccounts.persistence.dao.AccountsDao;
import uk.me.gumbley.simpleaccounts.persistence.dao.TransactionsDao;
import uk.me.gumbley.simpleaccounts.persistence.dao.impl.JdbcTemplateAccountsDao;
import uk.me.gumbley.simpleaccounts.persistence.dao.impl.JdbcTemplateTransactionsDao;

/**
 * The JdbcTemplate implementation of the SimpleAccountsDAOFactory.
 *
 * @author matt
 *
 */
public final class JdbcTemplateSimpleAccountsDAOFactory implements
        SimpleAccountsDAOFactory {

    private final JdbcTemplateAccountsDao mAccountsDao;
    private final JdbcTemplateTransactionsDao mTransactionsDao;

    /**
     * Construct the DAOFactory.
     *
     * @param jdbcTemplate the Jdbc Template.
     */
    public JdbcTemplateSimpleAccountsDAOFactory(final SimpleJdbcTemplate jdbcTemplate) {
        mAccountsDao = new JdbcTemplateAccountsDao(jdbcTemplate);
        mTransactionsDao = new JdbcTemplateTransactionsDao(jdbcTemplate);
        // cross-wire since transactionsdao needs to update accounts
        mTransactionsDao.setAccountsDao(mAccountsDao);
    }

    /**
     * {@inheritDoc}
     */
    public AccountsDao getAccountsDao() {
        return mAccountsDao;
    }

    /**
     * {@inheritDoc}
     */
    public TransactionsDao getTransactionsDao() {
        return mTransactionsDao;
    }
}
