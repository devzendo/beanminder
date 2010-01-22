package org.devzendo.simpleaccounts.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.devzendo.minimiser.util.Pair;
import org.devzendo.simpleaccounts.persistence.dao.TransactionsDao;
import org.devzendo.simpleaccounts.persistence.domain.Account;
import org.devzendo.simpleaccounts.persistence.domain.Transaction;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


/**
 * The JdbcTemplate implementation of the TransactionsDao.
 *
 * @author matt
 *
 */
public final class JdbcTemplateTransactionsDao implements TransactionsDao {
    private static final Logger LOGGER = Logger
            .getLogger(JdbcTemplateTransactionsDao.class);

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
    }

    /**
     * {@inheritDoc}
     */
    public Account deleteTransaction(final Account account, final Transaction transaction) {
        ensureAccountSaved(account);
        ensureTransactionSaved(transaction);
        // Always reload the account to get the correct balance.
        final Account reloadedAccount = mJdbcTemplateAccountsDao.loadAccount(account);
        LOGGER.debug("the reloaded account is " + account);

        // Calculate a delta to apply to the account and all subsequent transactions
        final Transaction reloadedTransaction = loadTransaction(transaction);
        LOGGER.debug("the reloaded transaction is " + reloadedTransaction);
        final int transactionAmountSigned = getSignedAmount(reloadedTransaction);
        LOGGER.debug("will subtract " + transactionAmountSigned + " from account and subsequent transactions");

        // Update the account with the new balance
        final Account newBalanceAccount = new Account(
            reloadedAccount.getId(), reloadedAccount.getName(), reloadedAccount.getAccountCode(),
            reloadedAccount.getWith(), reloadedAccount.getInitialBalance(),
            reloadedAccount.getCurrentBalance() - transactionAmountSigned);
        final Account updatedAccount = mJdbcTemplateAccountsDao.updateAccount(newBalanceAccount);
        LOGGER.debug("The updated account is " + updatedAccount);

        // Update all subsequent transactions, accountBalance -= transactionAmountSigned, index--
        final int numberOfTransactions = getNumberOfTransactions(reloadedAccount);
        deleteTransactionById(reloadedTransaction);
        for (int index = reloadedTransaction.getIndex() + 1; index < numberOfTransactions; index++) {
            subtractDeltaFromTransactionAccountBalanceAndDecrementIndexByIndex(updatedAccount, index, transactionAmountSigned);
        }


        return updatedAccount;
    }

    private void subtractDeltaFromTransactionAccountBalanceAndDecrementIndexByIndex(
            final Account account,
            final int transactionIndex,
            final int deltaToSubtract) {
        // TODO this seems overkill, and could be replaced by:
        // SELECT accountBalance FROM Transactions WHERE accountId = ? AND index = ?
        // UPDATE Transactions SET accountBalance = ?, index = ? WHERE accountId = ? AND id = ?
        final Transaction transaction = loadTransaction(account, transactionIndex);
        LOGGER.debug("tx#" + transactionIndex + " to apply delta of " + deltaToSubtract + " to is " + transaction);
        final Transaction updatedTransaction = new Transaction(
            transaction.getId(),
            transaction.getAccountId(),
            transactionIndex - 1,
            transaction.getAmount(),
            transaction.isCredit(),
            transaction.isReconciled(),
            transaction.getTransactionDate(),
            transaction.getAccountBalance() - deltaToSubtract);
        LOGGER.debug("saved, that's: " + updatedTransaction);
        updateTransaction(updatedTransaction);
    }

    private void deleteTransactionById(final Transaction transaction) {
        mJdbcTemplate.update(
            "DELETE FROM Transactions WHERE id = ?",
            new Object[] {transaction.getId()});
    }

    /**
     * {@inheritDoc}
     */
    public List<Transaction> findAllTransactionsForAccount(final Account account) {
        ensureAccountSaved(account);
        final String sql = "SELECT id, accountId, index, amount, isCredit, isReconciled, transactionDate, accountBalance "
            + "FROM Transactions WHERE accountId = ?"
            + "ORDER BY index ASC";
        final ParameterizedRowMapper<Transaction> mapper = createTransactionMapper();
        return mJdbcTemplate.query(sql, mapper, account.getId());
    }

    /**
     * {@inheritDoc}
     */
    public List<Transaction> findAllTransactionsForAccountByIndexRange(final Account account, final int fromIndex, final int toIndex) {
        ensureAccountSaved(account);
        final String sql = "SELECT id, accountId, index, amount, isCredit, isReconciled, transactionDate, accountBalance "
            + "FROM Transactions WHERE accountId = ? AND (index >= ? AND index <= ?)"
            + "ORDER BY index ASC";
        final ParameterizedRowMapper<Transaction> mapper = createTransactionMapper();
        return mJdbcTemplate.query(sql, mapper, account.getId(), fromIndex, toIndex);
    }

    /**
     * For use by the DAO layer, reload the transaction given its data that may be old
     * @param transaction the transaction to reload
     * @return the loaded transaction
     */
    Transaction loadTransaction(final Transaction transaction) {
        final String sql = "SELECT id, accountId, index, amount, isCredit, isReconciled, transactionDate, accountBalance "
            + "FROM Transactions WHERE id = ?";
        final ParameterizedRowMapper<Transaction> mapper = createTransactionMapper();
        return mJdbcTemplate.queryForObject(sql, mapper, transaction.getId());
    }

    /**
     * For use by the DAO layer, reload the transaction given its Account and index
     * @param account the Account for which the transaction should be loaded
     * @param index the index of the transaction within the account
     * @return the loaded transaction
     */
    Transaction loadTransaction(final Account account, final int index) {
        final String sql = "SELECT id, accountId, index, amount, isCredit, isReconciled, transactionDate, accountBalance "
            + "FROM Transactions WHERE accountId = ? AND index = ?";
        final ParameterizedRowMapper<Transaction> mapper = createTransactionMapper();
        return mJdbcTemplate.queryForObject(sql, mapper, account.getId(), index);
    }

    private ParameterizedRowMapper<Transaction> createTransactionMapper() {
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
        return mapper;
    }

    private void ensureAccountSaved(final Account account) {
        if (account.getId() == -1) {
            throw new DataIntegrityViolationException(
                "Cannot store a transaction against an unsaved account");
        }
    }

    private void ensureTransactionSaved(final Transaction transaction) {
        if (transaction.getId() == -1) {
            throw new DataIntegrityViolationException(
                "Cannot process an unsaved transaction");
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
            final Transaction updatedTransaction) {
        // Always reload the account to get the correct balance.
        final Account reloadedAccount = mJdbcTemplateAccountsDao.loadAccount(account);
        LOGGER.debug("the reloaded account is " + account);

        // Calculate a delta to apply to the account and all subsequent transactions
        final Transaction committedTransaction = loadTransaction(updatedTransaction);
        LOGGER.debug("the committed transaction is " + committedTransaction);
        LOGGER.debug("the updated transaction is   " + updatedTransaction);
        final int committedAmountSigned = getSignedAmount(committedTransaction);
        final int updatedAmountSigned = getSignedAmount(updatedTransaction);
        final int deltaToAdd = updatedAmountSigned - committedAmountSigned;
        LOGGER.debug("the delta to add (updated - committed) is " + deltaToAdd);

        // Update the account with the new balance
        final Account newBalanceAccount = new Account(
            reloadedAccount.getId(), reloadedAccount.getName(), reloadedAccount.getAccountCode(),
            reloadedAccount.getWith(), reloadedAccount.getInitialBalance(),
            reloadedAccount.getCurrentBalance() + deltaToAdd);
        final Account updatedAccount = mJdbcTemplateAccountsDao.updateAccount(newBalanceAccount);
        LOGGER.debug("The updated account is " + updatedAccount);

        // Update this transaction, by creating a new version of it from the committed version, not changing
        // any id or index fields.
        final Transaction newUpdatedTransaction = new Transaction(
            committedTransaction.getId(),
            committedTransaction.getAccountId(),
            committedTransaction.getIndex(),
            updatedTransaction.getAmount(),
            updatedTransaction.isCredit(),
            updatedTransaction.isReconciled(),
            updatedTransaction.getTransactionDate(),
            committedTransaction.getAccountBalance() + deltaToAdd);
        final Transaction savedUpdatedTransaction = updateTransaction(newUpdatedTransaction);
        LOGGER.debug("the saved updated transaction is " + savedUpdatedTransaction);

        // Update all subsequent transactions
        final int numberOfTransactions = getNumberOfTransactions(reloadedAccount);
        for (int index = savedUpdatedTransaction.getIndex() + 1; index < numberOfTransactions; index++) {
            addDeltaToTransactionAccountBalanceByIndex(updatedAccount, index, deltaToAdd);
        }

        return new Pair<Account, Transaction>(updatedAccount, savedUpdatedTransaction);
    }

    private void addDeltaToTransactionAccountBalanceByIndex(
            final Account account, final int index, final int deltaToAdd) {
        // TODO this seems overkill, and could be replaced by:
        // SELECT accountBalance FROM Transactions WHERE accountId = ? AND index = ?
        // UPDATE Transactions SET accountBalance = ? WHERE accountId = ? AND index = ?
        final Transaction transaction = loadTransaction(account, index);
        LOGGER.debug("tx#" + index + " to apply delta of " + deltaToAdd + " to is " + transaction);
        final Transaction updatedTransaction = new Transaction(
            transaction.getId(),
            transaction.getAccountId(),
            transaction.getIndex(),
            transaction.getAmount(),
            transaction.isCredit(),
            transaction.isReconciled(),
            transaction.getTransactionDate(),
            transaction.getAccountBalance() + deltaToAdd);
        LOGGER.debug("saved, that's: " + updatedTransaction);
        updateTransaction(updatedTransaction);
    }

    /**
     * Update a transaction with new data; does not change the account FK.
     * @param updatedTransaction the transaction to update
     * @return the updated transaction
     */
    Transaction updateTransaction(final Transaction updatedTransaction) {
        mJdbcTemplate.update(
            "UPDATE Transactions SET "
            + "index = ?, amount = ?, isCredit = ?, isReconciled = ?, transactionDate = ?, accountBalance = ? "
            + "WHERE id = ?",
            new Object[] {
                    updatedTransaction.getIndex(),
                    updatedTransaction.getAmount(),
                    updatedTransaction.isCredit(),
                    updatedTransaction.isReconciled(),
                    updatedTransaction.getTransactionDate(),
                    updatedTransaction.getAccountBalance(),
                    updatedTransaction.getId()});
        return updatedTransaction;
    }

    private int getSignedAmount(final Transaction transaction) {
        return (transaction.isCredit() ?
                transaction.getAmount() :
                (-1 * transaction.getAmount()));
    }

    private Pair<Account, Transaction> insertTransaction(
            final Account account,
            final Transaction transaction) {
        // Always reload the account to get the correct balance.
        final Account reloadedAccount = mJdbcTemplateAccountsDao.loadAccount(account);
        // Save the transaction
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final int transactionIndex = getNumberOfTransactions(reloadedAccount);
        final int newBalance = reloadedAccount.getCurrentBalance()
            + getSignedAmount(transaction);
        mJdbcTemplate.getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection conn)
                    throws SQLException {
                final String sql = "INSERT INTO Transactions "
                    + "(accountId, index, amount, isCredit, isReconciled, "
                    + "transactionDate, accountBalance) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
