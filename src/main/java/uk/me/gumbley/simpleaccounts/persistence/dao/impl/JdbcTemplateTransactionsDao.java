package uk.me.gumbley.simpleaccounts.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

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
    private JdbcTemplateAccountsDao mJdbcTemplateAccountsDao;

    /**
     * Construct the TransactionsDao with a JdbcTemplate
     * @param jdbcTemplate the template for database access
     */
    public JdbcTemplateTransactionsDao(final SimpleJdbcTemplate jdbcTemplate) {
        mJdbcTemplate = jdbcTemplate;
    }

    /**
     * Cross-wire the accounts DAO.
     * @param accountsDao the JdbcTemplateAccountsDao
     */
    public void setAccountsDao(final JdbcTemplateAccountsDao accountsDao) {
        mJdbcTemplateAccountsDao = accountsDao;
        // TODO Auto-generated method stub

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
        ensureAccountSaved(account);
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

    private void ensureAccountSaved(final Account account) {
        if (account.getId() == -1) {
            throw new DataIntegrityViolationException(
                "Cannot store a transaction against an unsaved account");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Pair<Account, Transaction> saveTransaction(
            final Account account,
            final Transaction transaction) {
        if (transaction.getAmount() < 0) {
            throw new IllegalArgumentException("Transaction amount must be positive, not "
                + transaction.getAmount());
        }
        ensureAccountSaved(account);
        if (transaction.getId() == -1) {
            return insertTransaction(account, transaction);
        } else {
            return updateTransaction(account, transaction);
        }
    }

    private Pair<Account, Transaction> updateTransaction(
            final Account account,
            final Transaction transaction) {
        // TODO Auto-generated method stub
        return null;
    }

    private Pair<Account, Transaction> insertTransaction(
            final Account account,
            final Transaction transaction) {
        // Save the transaction
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        /*
         *    "CREATE TABLE Transactions("
                + "id INT IDENTITY,"
                + "account_id INT NOT NULL,"
                + "amount INT NOT NULL,"
                + "isCredit BOOLEAN,"
                + "isReconciled BOOLEAN,"
                + "transactionDate DATE,"
                + ")",
         */
        mJdbcTemplate.getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection conn)
                    throws SQLException {
                final String sql = "INSERT INTO Transactions (account_id, amount, isCredit, isReconciled, transactiondate) values (?, ?, ?, ?, ?)";
                final PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"});
                ps.setInt(1, account.getId());
                ps.setInt(2, transaction.getAmount());
                ps.setBoolean(3, transaction.isCredit());
                ps.setBoolean(4, transaction.isReconciled());
                ps.setDate(5, transaction.getTransactionDate());
                return ps;
            }
        }, keyHolder);
        final int key = keyHolder.getKey().intValue();
        final Transaction savedTransaction = new Transaction(key, account.getId(),
            transaction.getAmount(), transaction.isCredit(), transaction.isReconciled(),
            transaction.getTransactionDate());
        // Update the account balance
        final int newBalance = account.getBalance() + (transaction.isCredit() ? transaction.getAmount() : (-1 * transaction.getAmount()));
        final Account newBalanceAccount = new Account(account.getId(), account.getName(), account.getAccountCode(), account.getWith(), newBalance);
        final Account updatedAccount = mJdbcTemplateAccountsDao.updateAccount(newBalanceAccount);
        return new Pair<Account, Transaction>(updatedAccount, savedTransaction);
    }
}
