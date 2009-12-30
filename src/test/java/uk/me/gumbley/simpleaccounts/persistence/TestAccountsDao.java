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
public final class TestAccountsDao extends SimpleAccountsDatabaseTest {
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
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
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
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
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
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
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
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
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
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
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
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
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
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
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
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
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

    private Account createSecondTestAccount() {
        final Account newAccount =
            new Account("Aardvark Test account", "867456",
                "Imaginary Bank of London", 50);
        return newAccount;
    }

    private Account saveTestAccount(
            final SimpleAccountsDAOFactory simpleAccountsDaoFactory, final Account account) {
        final Account savedAccount =
            simpleAccountsDaoFactory.getAccountsDao().saveAccount(account);
        return savedAccount;
    }

    private Date todayNormalised() {
        return SQLDateUtils.normalise(new Date(System.currentTimeMillis()));
    }
}
