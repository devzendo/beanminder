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
        final String sql = "select id, accountId, index, amount, isCredit, isReconciled, transactionDate, accountBalance "
            + "from Transactions where accountId = ?";
// TODO add test for this            + "order by index";
        final ParameterizedRowMapper<Transaction> mapper = new ParameterizedRowMapper<Transaction>() {
            public Transaction mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                return new Transaction(
                    rs.getInt("id"),
                    rs.getInt("accountId"),
                    rs.getInt("index"),
                    rs.getInt("amount"),
                    rs.getBoolean("isCredit"),
                    rs.getBoolean("isReconciled"),
                    rs.getDate("transactionDate"),
                    rs.getInt("accountBalance")
                    );
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
        if (transaction.getAmount() <= 0) {
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
        // Always reload the account to get the correct balance.
        final Account reloadedAccount = mJdbcTemplateAccountsDao.loadAccount(account);
        // Save the transaction
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        /*
         *    "CREATE TABLE Transactions("
                + "id INT IDENTITY,"
                + "accountId INT NOT NULL,"
                + "index INT NOT NULL,"
                + "amount INT NOT NULL,"
                + "isCredit BOOLEAN,"
                + "isReconciled BOOLEAN,"
                + "transactionDate DATE,"
                + "accountBalance INT"
                + ")",
         */
        final int transactionIndex = getNumberOfTransactions(reloadedAccount);
        final int newBalance = reloadedAccount.getCurrentBalance()
            + (transaction.isCredit() ?
                    transaction.getAmount() :
                    (-1 * transaction.getAmount()));
        mJdbcTemplate.getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection conn)
                    throws SQLException {
                final String sql = "INSERT INTO Transactions "
                    + "(accountId, index, amount, isCredit, isReconciled, "
                    + "transactionDate, accountBalance) values (?, ?, ?, ?, ?, ?, ?)";
                final PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"});
                ps.setInt(1, reloadedAccount.getId());
                ps.setInt(2, transactionIndex);
                ps.setInt(3, transaction.getAmount());
                ps.setBoolean(4, transaction.isCredit());
                ps.setBoolean(5, transaction.isReconciled());
                ps.setDate(6, transaction.getTransactionDate());
                ps.setInt(7, newBalance);
                return ps;
            }
        }, keyHolder);
        final int key = keyHolder.getKey().intValue();
        final Transaction savedTransaction = new Transaction(key, reloadedAccount.getId(),
            transactionIndex, transaction.getAmount(), transaction.isCredit(),
            transaction.isReconciled(), transaction.getTransactionDate(),
            newBalance);
        // Update the account balance
        final Account newBalanceAccount = new Account(
            reloadedAccount.getId(), reloadedAccount.getName(), reloadedAccount.getAccountCode(),
            reloadedAccount.getWith(), reloadedAccount.getInitialBalance(), newBalance);
        final Account updatedAccount = mJdbcTemplateAccountsDao.updateAccount(newBalanceAccount);
        return new Pair<Account, Transaction>(updatedAccount, savedTransaction);
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfTransactions(final Account account) {
        final int count = mJdbcTemplate.queryForInt(
            "SELECT COUNT(*) FROM Transactions WHERE accountId = ?",
            new Object[]{account.getId()});
        return count;
    }
}
