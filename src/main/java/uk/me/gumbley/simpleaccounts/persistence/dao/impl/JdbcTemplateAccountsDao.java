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

import uk.me.gumbley.simpleaccounts.persistence.dao.AccountsDao;
import uk.me.gumbley.simpleaccounts.persistence.domain.Account;

/**
 * The JdbcTemplate implementation of the AccountsDao
 *
 * @author matt
 *
 */
public final class JdbcTemplateAccountsDao implements AccountsDao {

    private final SimpleJdbcTemplate mJdbcTemplate;

    /**
     * Construct the AccountsDao with a JdbcTemplate
     * @param jdbcTemplate the template for database access
     */
    public JdbcTemplateAccountsDao(final SimpleJdbcTemplate jdbcTemplate) {
        mJdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAccount(final Account account) {
        if (account.getId() == -1) {
            throw new DataIntegrityViolationException(
                "Cannot delete an unsaved account");
        }
        final String sql = "DELETE FROM Accounts WHERE id = ?";
        mJdbcTemplate.update(sql, new Object[] {account.getId()});
    }

    /**
     * For use by the DAO layer, load an account given its data which may be old.
     * @param account the account to reload
     * @return the reloaded Account
     */
    Account loadAccount(final Account account) {
        final String sql = "SELECT id, name, with, accountCode, initialBalance, currentBalance FROM Accounts WHERE id = ?";
        final ParameterizedRowMapper<Account> mapper = createAccountMapper();
        return mJdbcTemplate.queryForObject(sql, mapper, account.getId());
    }

    /**
     * {@inheritDoc}
     */
    public List<Account> findAllAccounts() {
        final String sql = "SELECT id, name, with, accountCode, initialBalance, currentBalance FROM Accounts ORDER BY name";
        final ParameterizedRowMapper<Account> mapper = createAccountMapper();
        return mJdbcTemplate.query(sql, mapper);
    }

    private ParameterizedRowMapper<Account> createAccountMapper() {
        final ParameterizedRowMapper<Account> mapper = new ParameterizedRowMapper<Account>() {

            // notice the return type with respect to Java 5 covariant return types
            public Account mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                final Account account = new Account(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("accountCode"),
                    rs.getString("with"),
                    rs.getInt("initialBalance"),
                    rs.getInt("currentBalance"));
                return account;
            }
        };
        return mapper;
    }

    /**
     * {@inheritDoc}
     */
    public Account saveAccount(final Account account) {
        if (account.getId() != -1) {
            return updateAccount(account);
        } else {
            return insertAccount(account);
        }
    }

    private Account insertAccount(final Account account) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        mJdbcTemplate.getJdbcOperations().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection conn)
                    throws SQLException {
                final String sql = "INSERT INTO Accounts "
                + "(name, with, accountCode, initialBalance, currentBalance) "
                + "VALUES (?, ?, ?, ?, ?)";
                final PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"});
                ps.setString(1, account.getName());
                ps.setString(2, account.getWith());
                ps.setString(3, account.getAccountCode());
                ps.setInt(4, account.getInitialBalance());
                ps.setInt(5, account.getCurrentBalance());
                return ps;
            }
        }, keyHolder);
        final int key = keyHolder.getKey().intValue();
        return new Account(
            key, account.getName(), account.getAccountCode(),
            account.getWith(), account.getInitialBalance(), account.getCurrentBalance());
    }

    /**
     * Update an existing account
     * @param account the account to update, with a valid id
     * @return the updated account (actually, the same as the input)
     */
    Account updateAccount(final Account account) {
        mJdbcTemplate.update(
            "UPDATE Accounts SET name = ?, with = ?, accountCode = ?, currentBalance = ?"
            + "WHERE id = ?",
            new Object[] {account.getName(), account.getWith(), account.getAccountCode(),
                    account.getCurrentBalance(), account.getId()});
        return account;
    }
}
