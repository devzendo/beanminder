/**
 * Copyright (C) 2008-2010 Matt Gumbley, DevZendo.org <http://devzendo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devzendo.beanminder.persistence;

import java.sql.Date;
import java.util.List;

import org.devzendo.beanminder.persistence.BeanMinderDAOFactory;
import org.devzendo.beanminder.persistence.dao.AccountsDao;
import org.devzendo.beanminder.persistence.dao.TransactionsDao;
import org.devzendo.beanminder.persistence.domain.Account;
import org.devzendo.beanminder.persistence.domain.Transaction;
import org.devzendo.minimiser.persistence.DAOFactory;
import org.devzendo.minimiser.persistence.MiniMiserDAOFactory;
import org.devzendo.minimiser.util.InstanceSet;
import org.devzendo.minimiser.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;



/**
 * Tests the correct operation of the DAO Layer's AccountsDao.
 *
 * @author matt
 *
 */
public final class TestAccountsDao extends BeanMinderDatabaseTest {
    /**
     *
     */
    @Test
    public void createEmptyAccount() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final AccountsDao accountsDao = simpleAccountsDaoFactory.getAccountsDao();
        final Account newAccount = createTestAccount();
        final Account savedAccount =
            accountsDao.saveAccount(newAccount);

        Assert.assertTrue(savedAccount.getId() > 0);
        Assert.assertEquals(newAccount.getAccountCode(), savedAccount.getAccountCode());
        Assert.assertEquals(newAccount.getName(), savedAccount.getName());
        Assert.assertEquals(newAccount.getWith(), savedAccount.getWith());
        Assert.assertEquals(newAccount.getCurrentBalance(), savedAccount.getCurrentBalance());
        Assert.assertEquals(newAccount.getInitialBalance(), savedAccount.getInitialBalance());

        // The current balance is initialised to the initial balance
        Assert.assertEquals(savedAccount.getInitialBalance(), savedAccount.getCurrentBalance());

        final TransactionsDao transactionsDao =
            simpleAccountsDaoFactory.getTransactionsDao();

        Assert.assertEquals(0,
            transactionsDao.
            findAllTransactionsForAccount(savedAccount).
            size());
    }

    /**
     *
     */
    @Test
    public void someAccountDetailsCanBeChanged() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final AccountsDao accountsDao =
            simpleAccountsDaoFactory.getAccountsDao();

        final Account newAccount = createTestAccount();
        Account savedAccount =
            accountsDao.saveAccount(newAccount);

        savedAccount.setName("My bank account");
        savedAccount = accountsDao.saveAccount(savedAccount);
        Assert.assertEquals("My bank account", savedAccount.getName());

        savedAccount.setAccountCode("0101010");
        savedAccount = accountsDao.saveAccount(savedAccount);
        Assert.assertEquals("0101010", savedAccount.getAccountCode());

        savedAccount.setWith("Another bank");
        savedAccount = accountsDao.saveAccount(savedAccount);
        Assert.assertEquals("Another bank", savedAccount.getWith());

        // These don't change
        Assert.assertEquals(5600, savedAccount.getCurrentBalance());
        Assert.assertEquals(5600, savedAccount.getInitialBalance());
    }


    /**
     *
     */
    @Test(expected = DataAccessException.class)
    public void cannotCommitTransactionAgainstUnsavedAccount() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        // note: unsaved Account
        simpleAccountsDaoFactory.
            getTransactionsDao().
            findAllTransactionsForAccount(newAccount);
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void cannotCommitTransactionsWithNegativeAmounts() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory, newAccount);

        final Transaction newTransaction = new Transaction(-200, false, false, todayNormalised());
        simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
    }

    /**
     *
     */
    @Test(expected = IllegalArgumentException.class)
    public void cannotCommitTransactionsWithZeroAmounts() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory, newAccount);

        final Transaction newTransaction = new Transaction(0, false, false, todayNormalised());
        simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
    }

    /**
     *
     */
    @Test
    public void transactionCanBeAddedToAccount() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory, newAccount);

        final Date todayNormalised = todayNormalised();
        final Transaction newTransaction = new Transaction(200, true, false, todayNormalised);
        final Pair<Account, Transaction> pair = simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
        final Account updatedAccount = pair.getFirst();

        final Transaction savedTransaction = pair.getSecond();
        Assert.assertTrue(savedTransaction.getId() > 0);
        Assert.assertEquals(updatedAccount.getId(), savedTransaction.getAccountId());
        Assert.assertEquals(200, savedTransaction.getAmount());
        Assert.assertTrue(savedTransaction.isCredit());
        Assert.assertFalse(savedTransaction.isReconciled());
        Assert.assertEquals(todayNormalised, savedTransaction.getTransactionDate());

        final List<Transaction> transactions = simpleAccountsDaoFactory.getTransactionsDao().findAllTransactionsForAccount(updatedAccount);
        Assert.assertEquals(1, transactions.size());
        System.out.println("saved transaction is " + savedTransaction.hashCode());
        System.out.println("returned transaction is " + transactions.get(0).hashCode());
        Assert.assertEquals(savedTransaction, transactions.get(0));
    }

    /**
     *
     */
    @Test
    public void addCreditTransactionToAccountIncreasesBalance() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory, newAccount);

        final Transaction newTransaction = new Transaction(200, true, false, todayNormalised());
        final Pair<Account, Transaction> pair = simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
        Assert.assertEquals(5800, pair.getFirst().getCurrentBalance());
        // initial balance should not change
        Assert.assertEquals(5600, pair.getFirst().getInitialBalance());
    }

    /**
     *
     */
    @Test
    public void addDebitTransactionToAccountDecreasesBalance() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory, newAccount);

        final Transaction newTransaction = new Transaction(200, false, false, todayNormalised());
        final Pair<Account, Transaction> pair = simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
        Assert.assertEquals(5400, pair.getFirst().getCurrentBalance());
        // initial balance should not change
        Assert.assertEquals(5600, pair.getFirst().getInitialBalance());
    }

    /**
     *
     */
    @Test
    public void accountsCanBeListed() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account accountOne = createTestAccount();
        final Account savedAccountOne = saveTestAccount(simpleAccountsDaoFactory, accountOne);
        final Account accountTwo = createSecondTestAccount();
        final Account savedAccountTwo = saveTestAccount(simpleAccountsDaoFactory, accountTwo);

        final List<Account> allAccounts = simpleAccountsDaoFactory.getAccountsDao().findAllAccounts();
        Assert.assertEquals(2, allAccounts.size());
        Assert.assertFalse(allAccounts.get(0).equals(allAccounts.get(1)));
        // ordered by name
        Assert.assertTrue(allAccounts.get(0).equals(savedAccountTwo));
        Assert.assertTrue(allAccounts.get(1).equals(savedAccountOne));
    }

    /**
     *
     */
    @Test
    public void deleteAccountDeletesAccountAndAllReferencedTransactions() {
        final InstanceSet<DAOFactory> daoFactories = createTestDatabaseReturningAllDAOFactories();
        final BeanMinderDAOFactory simpleAccountsDaoFactory = daoFactories.getInstanceOf(BeanMinderDAOFactory.class);
        final MiniMiserDAOFactory minimiserDaoFactory = daoFactories.getInstanceOf(MiniMiserDAOFactory.class);
        final SimpleJdbcTemplate simpleJdbcTemplate = minimiserDaoFactory.getSQLAccess().getSimpleJdbcTemplate();

        final Account account = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory, account);
        final Transaction newTransaction = new Transaction(200, true, false, todayNormalised());
        final Pair<Account, Transaction> pair = simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
        final Transaction savedTransaction = pair.getSecond();
        checkAccountExistence(simpleJdbcTemplate, savedAccount.getId(), true);
        checkTransactionExistence(simpleJdbcTemplate, savedTransaction.getId(), true);

        simpleAccountsDaoFactory.getAccountsDao().deleteAccount(savedAccount);

        checkAccountExistence(simpleJdbcTemplate, savedAccount.getId(), false);
        checkTransactionExistence(simpleJdbcTemplate, savedTransaction.getId(), false);
    }

    private void checkTransactionExistence(final SimpleJdbcTemplate simpleJdbcTemplate, final int id, final boolean exists) {
        Assert.assertTrue(id != -1);
        final int numTransactions = simpleJdbcTemplate.queryForInt(
            "SELECT COUNT(*) FROM Transactions WHERE id = ?", new Object[]{id});
        Assert.assertEquals(exists, numTransactions == 1);
    }

    private void checkAccountExistence(final SimpleJdbcTemplate simpleJdbcTemplate, final int id, final boolean exists) {
        Assert.assertTrue(id != -1);
        final int numAccounts = simpleJdbcTemplate.queryForInt(
            "SELECT COUNT(*) FROM Accounts WHERE id = ?", new Object[]{id});
        Assert.assertEquals(exists, numAccounts == 1);
    }

    /**
     *
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void cannotDeleteAnUnsavedAccount() {
        final BeanMinderDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        simpleAccountsDaoFactory.getAccountsDao().deleteAccount(createTestAccount());
    }
}
