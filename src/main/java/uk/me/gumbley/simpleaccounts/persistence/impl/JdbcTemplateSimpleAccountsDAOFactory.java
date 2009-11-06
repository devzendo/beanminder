package uk.me.gumbley.simpleaccounts.persistence.impl;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import uk.me.gumbley.simpleaccounts.persistence.SimpleAccountsDAOFactory;
import uk.me.gumbley.simpleaccounts.persistence.dao.AccountsDao;
import uk.me.gumbley.simpleaccounts.persistence.dao.TransactionsDao;

/**
 * The JdbcTemplate implementation of the SimpleAccountsDAOFactory.
 *
 * @author matt
 *
 */
public final class JdbcTemplateSimpleAccountsDAOFactory implements
        SimpleAccountsDAOFactory {

    /**
     * Construct the DAOFactory.
     *
     * @param jdbcTemplate the Jdbc Template.
     */
    public JdbcTemplateSimpleAccountsDAOFactory(final SimpleJdbcTemplate jdbcTemplate) {
        // TODO Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     */
    public AccountsDao getAccountsDao() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public TransactionsDao getTransactionsDao() {
        // TODO Auto-generated method stub
        return null;
    }
}
