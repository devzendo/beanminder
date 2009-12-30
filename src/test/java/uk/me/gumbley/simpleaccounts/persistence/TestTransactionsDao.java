package uk.me.gumbley.simpleaccounts.persistence;

import java.sql.Date;

import junit.framework.Assert;

import org.junit.Test;

import uk.me.gumbley.minimiser.util.Pair;
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
    public void transactionsHaveMonotonicallyIncreasingIndexAndCorrectAccountBalance() {
        final SimpleAccountsDAOFactory simpleAccountsDaoFactory = createTestDatabase();
        final Account newAccount = createTestAccount();
        final Account savedAccount = saveTestAccount(simpleAccountsDaoFactory,
            newAccount);
        final Date todayNormalised = todayNormalised();
        // Transaction 1
        final Transaction newTransaction1 = new Transaction(200, true, false,
                todayNormalised);
        Assert.assertEquals(-1, newTransaction1.getIndex()); // HMMM internal detail?
        Assert.assertEquals(-1, newTransaction1.getAccountBalance()); // HMMM internal detail?
        final Pair<Account, Transaction> pair1 = simpleAccountsDaoFactory
                .getTransactionsDao().saveTransaction(savedAccount,
                    newTransaction1);
        
        final Transaction savedTransaction1 = pair1.getSecond();
        Assert.assertEquals(0, savedTransaction1.getIndex());
        Assert.assertEquals(5800, savedTransaction1.getAccountBalance());
        
        // Transaction 2
        final Transaction newTransaction2 = new Transaction(20, true, false,
            todayNormalised);
        final Pair<Account, Transaction> pair2 = simpleAccountsDaoFactory
                .getTransactionsDao().saveTransaction(savedAccount,
                    newTransaction2);
        
        final Transaction savedTransaction2 = pair2.getSecond();
        Assert.assertEquals(1, savedTransaction2.getIndex());
        Assert.assertEquals(5820, savedTransaction2.getAccountBalance());
            
    }
}
