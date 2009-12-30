package uk.me.gumbley.simpleaccounts.persistence;

import java.sql.Date;

import junit.framework.Assert;

import org.junit.Test;

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
}
