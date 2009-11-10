package uk.me.gumbley.simpleaccounts.persistence.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
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
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    public List<Account> findAllAccounts() {
        // TODO Auto-generated method stub
        return null;
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
                final String sql = "INSERT INTO Accounts (name, with, accountCode, balance) values (?, ?, ?, ?)";
                final PreparedStatement ps = conn.prepareStatement(sql, new String[] {"id"});
                ps.setString(1, account.getName());
                ps.setString(2, account.getWith());
                ps.setString(3, account.getAccountCode());
                ps.setInt(4, account.getBalance());
                return ps;
            }
        }, keyHolder);
        final int key = keyHolder.getKey().intValue();
        return new Account(key, account.getName(), account.getAccountCode(), account.getWith(), account.getBalance());
    }

    private Account updateAccount(final Account account) {
        // TODO Auto-generated method stub
        return null;
    }
}
