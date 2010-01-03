package uk.me.gumbley.simpleaccounts.persistence;

import java.sql.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import uk.me.gumbley.minimiser.util.Pair;
import uk.me.gumbley.simpleaccounts.persistence.dao.TransactionsDao;
import uk.me.gumbley.simpleaccounts.persistence.domain.Account;
import uk.me.gumbley.simpleaccounts.persistence.domain.Transaction;

/**
 * Tests for the correct operation of the DAO Layer's TransactionDao.
 *
 * @author matt
 *
 */
public final class TestTransactionsDao extends SimpleAccountsDatabaseTest {

    /**
     * 
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void transactionsCannotBeAddedToANewAccount() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        simpleAccountsDaoFactory.getTransactionsDao().saveTransaction(createTestAccount(),
            new Transaction(200, true, false, todayNormalised()));
    }

    /**
     *
     */
    @Test
    public void numberOfTransactionsIsCorrectAfterSavingTransactions() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final Date todayNormalised = todayNormalised();
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        Assert.assertEquals(0, transactionsDao.getNumberOfTransactions(savedAccount));
        transactionsDao.saveTransaction(savedAccount,
            new Transaction(200, true, false, todayNormalised));
        Assert.assertEquals(1, transactionsDao.getNumberOfTransactions(savedAccount));
        transactionsDao.saveTransaction(savedAccount,
            new Transaction(400, true, false, todayNormalised));
        Assert.assertEquals(2, transactionsDao.getNumberOfTransactions(savedAccount));
    }

    /**
     *
     */
    @Test
    public void transactionsHaveMonotonicallyIncreasingIndexAndCorrectAccountBalance() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount(); // balance 5600
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final Date todayNormalised = todayNormalised();
        // Transaction 1
        final Transaction newTransaction1 = new Transaction(200, true, false,
                todayNormalised); // +200 = 5800
        Assert.assertEquals(-1, newTransaction1.getIndex()); // HMMM internal detail?
        Assert.assertEquals(-1, newTransaction1.getAccountBalance()); // HMMM internal detail?

        final Pair<Account, Transaction> pair1 = simpleAccountsDaoFactory
                .getTransactionsDao().saveTransaction(savedAccount,
                    newTransaction1);
        final Account savedAccount1 = pair1.getFirst();
        final Transaction savedTransaction1 = pair1.getSecond();
        Assert.assertEquals(0, savedTransaction1.getIndex());
        Assert.assertEquals(5800, savedTransaction1.getAccountBalance());
        Assert.assertEquals(5800, savedAccount1.getCurrentBalance());

        // Transaction 2
        final Transaction newTransaction2 = new Transaction(20, true, false,
            todayNormalised); // +20 = 5820
        final Pair<Account, Transaction> pair2 = simpleAccountsDaoFactory
                .getTransactionsDao().saveTransaction(savedAccount,
                    newTransaction2);
        final Account savedAccount2 = pair2.getFirst();
        final Transaction savedTransaction2 = pair2.getSecond();
        Assert.assertEquals(1, savedTransaction2.getIndex());
        Assert.assertEquals(5820, savedTransaction2.getAccountBalance());
        Assert.assertEquals(5820, savedAccount2.getCurrentBalance());

        // Transaction 3
        final Transaction newTransaction3 = new Transaction(10, false, false,
            todayNormalised); // -10 = 5810
        final Pair<Account, Transaction> pair3 = simpleAccountsDaoFactory
                .getTransactionsDao().saveTransaction(savedAccount,
                    newTransaction3);
        final Account savedAccount3 = pair3.getFirst();
        final Transaction savedTransaction3 = pair3.getSecond();
        Assert.assertEquals(2, savedTransaction3.getIndex());
        Assert.assertEquals(5810, savedTransaction3.getAccountBalance());
        Assert.assertEquals(5810, savedAccount3.getCurrentBalance());
    }
    
    /**
     * 
     */
    @Test
    public void transactionsAreListedOrderedByIndex() {
        // if the select is not ordered by index, this insertion seems to yield
        // a list in the order inserted here, but it needs the ORDER BY index
        // ASC for correctness, so I'll test for it anyway
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        final Date todayNormalised = todayNormalised();
        // Transaction 1
        final Transaction newTransaction1 = new Transaction(200, true, false,
                todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction1);

        // Transaction 2
        final Transaction newTransaction2 = new Transaction(20, true, false,
            todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction2);

        // Transaction 3
        final Transaction newTransaction3 = new Transaction(10, false, false,
            todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction3);
        
        final List<Transaction> allTransactions = transactionsDao.findAllTransactionsForAccount(savedAccount);
        Assert.assertEquals(200, allTransactions.get(0).getAmount());
        Assert.assertEquals(0, allTransactions.get(0).getIndex());
        Assert.assertEquals(20, allTransactions.get(1).getAmount());
        Assert.assertEquals(1, allTransactions.get(1).getIndex());
        Assert.assertEquals(10, allTransactions.get(2).getAmount());
        Assert.assertEquals(2, allTransactions.get(2).getIndex());
    }
    
    /**
     * 
     */
    @Test
    public void updateATransactionAndSubsequentTransactionsAndAccountBalanceUpdated() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final TransactionsDao transactionsDao = simpleAccountsDaoFactory.getTransactionsDao();
        final Date todayNormalised = todayNormalised();
        // Transaction 1
        final Transaction newTransaction1 = new Transaction(200, true, false,
                todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction1);

        // Transaction 2
        final Transaction newTransaction2 = new Transaction(20, true, false,
            todayNormalised);
        final Pair<Account, Transaction> savedTransaction2Pair = 
            transactionsDao.saveTransaction(savedAccount, newTransaction2);
        final Transaction savedTransaction2 = savedTransaction2Pair.getSecond();

        // Transaction 3
        final Transaction newTransaction3 = new Transaction(10, false, false,
            todayNormalised);
        transactionsDao.saveTransaction(savedAccount, newTransaction3);
        
        // Update Transaction 2
        savedTransaction2.setAmount(30); // +10
        transactionsDao.saveTransaction(savedAccount, savedTransaction2);
        
        // Has the account been modified?
        final Account reloadedAccount = savedTransaction2Pair.getFirst();
        Assert.assertEquals(5820, reloadedAccount.getCurrentBalance());
        
        final List<Transaction> allTransactions = transactionsDao.findAllTransactionsForAccount(savedAccount);
        Assert.assertEquals(200, allTransactions.get(0).getAmount());
        Assert.assertEquals(0, allTransactions.get(0).getIndex());
        Assert.assertEquals(5800, allTransactions.get(0).getAccountBalance());
        Assert.assertEquals(30, allTransactions.get(1).getAmount());
        Assert.assertEquals(1, allTransactions.get(1).getIndex());
        Assert.assertEquals(5830, allTransactions.get(1).getAccountBalance());
        Assert.assertEquals(10, allTransactions.get(2).getAmount());
        Assert.assertEquals(2, allTransactions.get(2).getIndex());
        Assert.assertEquals(5820, allTransactions.get(2).getAccountBalance());
    }
    
    @Test
    public void updateATransactionByAlsoChangingCreditDebitFlag() {
        Assert.fail("unfinished");
    }
}
