package uk.me.gumbley.simpleaccounts.persistence.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import uk.me.gumbley.minimiser.util.Pair;
import uk.me.gumbley.simpleaccounts.persistence.dao.TransactionsDao;
import uk.me.gumbley.simpleaccounts.persistence.domain.Account;
import uk.me.gumbley.simpleaccounts.persistence.domain.Transaction;

/**
 * The JdbcTemplate implementation of the TransactionsDao.
 *
 * @author matt
 *
 */
public final class JdbcTemplateTransactionsDao implements TransactionsDao {

    private final SimpleJdbcTemplate mJdbcTemplate;

    public JdbcTemplateTransactionsDao(final SimpleJdbcTemplate jdbcTemplate) {
        mJdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    public Account deleteTransaction(final Account account, final Transaction transaction) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<Transaction> findAllTransactionsForAccount(final Account account) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Pair<Account, Transaction> saveTransaction(
            final Account account,
            final Transaction transaction) {
        // TODO Auto-generated method stub
        return null;
    }
}
