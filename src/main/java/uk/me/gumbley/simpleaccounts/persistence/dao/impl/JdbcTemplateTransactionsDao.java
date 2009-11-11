package uk.me.gumbley.simpleaccounts.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
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

    /**
     * Construct the TransactionsDao with a JdbcTemplate
     * @param jdbcTemplate the template for database access
     */
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
        // TODO if account.id == -1, return empty list
        final String sql = "select id, account_id, amount, isCredit, isReconciled, transactionDate "
            + "from Transactions where account_id = ?";
        final ParameterizedRowMapper<Transaction> mapper = new ParameterizedRowMapper<Transaction>() {
            public Transaction mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                return new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getInt("amount"),
                    rs.getBoolean("isCredit"),
                    rs.getBoolean("isReconciled"),
                    rs.getDate("transactionDate"));
            }
        };
        return mJdbcTemplate.query(sql, mapper, account.getId());
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
