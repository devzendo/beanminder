package uk.me.gumbley.simpleaccounts.persistence;

import java.sql.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.dao.DataAccessException;

import uk.me.gumbley.commoncode.datetime.SQLDateUtils;
import uk.me.gumbley.minimiser.persistence.DAOFactory;
import uk.me.gumbley.minimiser.util.InstanceSet;
import uk.me.gumbley.minimiser.util.Pair;
import uk.me.gumbley.simpleaccounts.persistence.dao.AccountsDao;
import uk.me.gumbley.simpleaccounts.persistence.dao.TransactionsDao;
import uk.me.gumbley.simpleaccounts.persistence.domain.Account;
import uk.me.gumbley.simpleaccounts.persistence.domain.Transaction;


/**
 * Tests the correct operation of the DAO Layer.
 *
 * @author matt
 *
 */
public final class DAOLayerTests extends SimpleAccountsDatabaseTest {
    /**
     *
     */
    @Test
    public void createEmptyAccount() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final AccountsDao accountsDao =
            simpleAccountsDaoFactory.getAccountsDao();

        final Account newAccount = createTestAccount();
        final Account savedAccount =
            accountsDao.saveAccount(newAccount);

        Assert.assertTrue(savedAccount.getId() > 0);
        Assert.assertEquals(newAccount.getAccountCode(), savedAccount.getAccountCode());
        Assert.assertEquals(newAccount.getName(), savedAccount.getName());
        Assert.assertEquals(newAccount.getWith(), savedAccount.getWith());
        Assert.assertEquals(newAccount.getBalance(), savedAccount.getBalance());

        final TransactionsDao transactionsDao =
            simpleAccountsDaoFactory.getTransactionsDao();

        Assert.assertEquals(0,
            transactionsDao.
            findAllTransactionsForAccount(savedAccount).
            size());
    }



    @Test(expected = DataAccessException.class)
    public void cannotCommitTransactionAgainstUnsavedAccount() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        // note: unsaved Account
        simpleAccountsDaoFactory.
            getTransactionsDao().
            findAllTransactionsForAccount(newAccount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCommitTransactionsWithNegativeAmounts() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account savedAccount = createAndSaveTestAccount(simpleAccountsDaoFactory);

        final Transaction newTransaction = new Transaction(-200, false, false, todayNormalised());
        simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotCommitTransactionsWithZeroAmounts() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account savedAccount = createAndSaveTestAccount(simpleAccountsDaoFactory);

        final Transaction newTransaction = new Transaction(0, false, false, todayNormalised());
        simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
    }

    @Test
    public void transactionCanBeAddedToAccount() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account savedAccount = createAndSaveTestAccount(simpleAccountsDaoFactory);

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
        Assert.assertEquals(savedTransaction, transactions.get(0));
    }

    @Test
    public void addCreditTransactionToAccountIncreasesBalance() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account savedAccount = createAndSaveTestAccount(simpleAccountsDaoFactory);

        final Transaction newTransaction = new Transaction(200, true, false, todayNormalised());
        final Pair<Account, Transaction> pair = simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
        Assert.assertEquals(5800, pair.getFirst().getBalance());
    }

    @Test
    public void addDebitTransactionToAccountIncreasesBalance() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account savedAccount = createAndSaveTestAccount(simpleAccountsDaoFactory);

        final Transaction newTransaction = new Transaction(200, false, false, todayNormalised());
        final Pair<Account, Transaction> pair = simpleAccountsDaoFactory.getTransactionsDao().
            saveTransaction(savedAccount, newTransaction);
        Assert.assertEquals(5400, pair.getFirst().getBalance());
    }

    private SimpleAccountsDAOFactory createTestDatabase() {
        final InstanceSet<DAOFactory> database =
            getPersistencePluginHelper().createDatabase(
                DBNAME, DBPASSWORD);
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory =
            database.getInstanceOf(SimpleAccountsDAOFactory.class);
        return simpleAccountsDaoFactory;
    }

    private Account createTestAccount() {
        final Account newAccount =
            new Account("Test account", "123456",
                "Imaginary Bank of London", 5600);
        return newAccount;
    }

    private Account createAndSaveTestAccount(
            final SimpleAccountsDAOFactory simpleAccountsDaoFactory) {
        final Account newAccount = createTestAccount();
        final Account savedAccount =
            simpleAccountsDaoFactory.getAccountsDao().saveAccount(newAccount);
        return savedAccount;
    }

    private Date todayNormalised() {
        return SQLDateUtils.normalise(new Date(System.currentTimeMillis()));
    }
}
